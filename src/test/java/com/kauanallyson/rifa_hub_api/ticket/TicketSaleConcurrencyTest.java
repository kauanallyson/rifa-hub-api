package com.kauanallyson.rifa_hub_api.ticket;

import com.kauanallyson.rifa_hub_api.client.ClientService;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientCreate;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientResponse;
import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeCreate;
import com.kauanallyson.rifa_hub_api.raffle.RaffleService;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleCreate;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleResponse;
import com.kauanallyson.rifa_hub_api.seller.SellerService;
import com.kauanallyson.rifa_hub_api.seller.dtos.SellerCreate;
import com.kauanallyson.rifa_hub_api.seller.dtos.SellerResponse;
import com.kauanallyson.rifa_hub_api.shared.enums.TicketStatus;
import com.kauanallyson.rifa_hub_api.support.IntegrationTest;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketOrderRequest;
import com.kauanallyson.rifa_hub_api.ticket.dtos.TicketResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Disabled("Executable spec for the ticket sale deepening. Currently fails: both buyers win. "
        + "Remove @Disabled once the sale decides availability under a lock or a conditional update.")
class TicketSaleConcurrencyTest {

    private static final int TICKET_NUMBER = 1;

    @Autowired
    private RaffleService raffleService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private TicketService ticketService;

    @Test
    void twoBuyersRacingForTheSameTicketOnlyOneWins() throws Exception {
        RaffleResponse raffle = raffleService.createRaffle(new RaffleCreate(
                "Rifa do Joao Pedro " + System.nanoTime(),
                "concurrency probe",
                BigDecimal.TEN,
                10L,
                LocalDateTime.now().plusDays(7),
                List.of(new PrizeCreate("Primeiro premio", 1))));

        SellerResponse seller = sellerService.createSeller(new SellerCreate(
                "Maria Silva " + System.nanoTime(), phone(), email("maria")));
        ClientResponse first = clientService.createClient(new ClientCreate(
                "Joao Pedro", phone(), email("joao")));
        ClientResponse second = clientService.createClient(new ClientCreate(
                "Pedro Henrique", phone(), email("pedro")));

        CountDownLatch startLine = new CountDownLatch(1);
        AtomicInteger succeeded = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> buyerOne = pool.submit(buyTask(raffle.id(), first.id(), seller.id(), startLine, succeeded));
        Future<?> buyerTwo = pool.submit(buyTask(raffle.id(), second.id(), seller.id(), startLine, succeeded));

        startLine.countDown();
        buyerOne.get(30, TimeUnit.SECONDS);
        buyerTwo.get(30, TimeUnit.SECONDS);
        pool.shutdown();

        assertThat(succeeded.get())
                .as("exactly one buyer should be able to buy ticket %d", TICKET_NUMBER)
                .isEqualTo(1);

        List<TicketResponse> sold = ticketService.getTicketsByRaffleId(raffle.id(), TicketStatus.SOLD);
        assertThat(sold)
                .as("the ticket should have exactly one owner")
                .hasSize(1);
    }

    private Runnable buyTask(Long raffleId, Long clientId, Long sellerId,
                             CountDownLatch startLine, AtomicInteger succeeded) {
        return () -> {
            try {
                startLine.await();
                ticketService.sellTickets(raffleId,
                        new TicketOrderRequest(List.of(TICKET_NUMBER), clientId, sellerId));
                succeeded.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RuntimeException expectedForTheLoser) {
                // the losing buyer should be rejected
            }
        };
    }

    private String phone() {
        return String.valueOf(System.nanoTime()).substring(0, 11);
    }

    private String email(String prefix) {
        return prefix + System.nanoTime() + "@example.com";
    }
}
