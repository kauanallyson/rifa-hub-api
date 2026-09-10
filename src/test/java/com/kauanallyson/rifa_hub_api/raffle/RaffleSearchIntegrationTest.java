package com.kauanallyson.rifa_hub_api.raffle;

import com.kauanallyson.rifa_hub_api.prize.dtos.PrizeCreate;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleCreate;
import com.kauanallyson.rifa_hub_api.raffle.dtos.RaffleResponse;
import com.kauanallyson.rifa_hub_api.shared.enums.RaffleStatus;
import com.kauanallyson.rifa_hub_api.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class RaffleSearchIntegrationTest {

    @Autowired
    private RaffleService raffleService;

    @Autowired
    private RaffleRepository raffleRepository;

    @BeforeEach
    void clearRaffles() {
        raffleRepository.deleteAll();
    }

    @Test
    void searchMatchesNameAndDescriptionAndIgnoresCase() {
        create("Rifa do Joao Pedro", "Sorteio beneficente da escola");
        create("Rifa da Maria Silva", "Campanha do agasalho");

        assertThat(raffleService.findAll("joao", null))
                .extracting(RaffleResponse::name)
                .containsExactly("Rifa do Joao Pedro");

        assertThat(raffleService.findAll("JOAO", null))
                .extracting(RaffleResponse::name)
                .containsExactly("Rifa do Joao Pedro");

        assertThat(raffleService.findAll("agasalho", null))
                .extracting(RaffleResponse::name)
                .containsExactly("Rifa da Maria Silva");

        assertThat(raffleService.findAll("rifa", null)).hasSize(2);
        assertThat(raffleService.findAll("bicicleta", null)).isEmpty();
    }

    @Test
    void filtersAreIndependentAndOptional() {
        create("Rifa do Joao Pedro", "Sorteio beneficente da escola");
        create("Rifa da Maria Silva", "Campanha do agasalho");

        assertThat(raffleService.findAll(null, null)).hasSize(2);
        assertThat(raffleService.findAll("   ", null)).hasSize(2);
        assertThat(raffleService.findAll(null, RaffleStatus.OPEN)).hasSize(2);
        assertThat(raffleService.findAll(null, RaffleStatus.FINISHED)).isEmpty();

        assertThat(raffleService.findAll("joao", RaffleStatus.OPEN))
                .extracting(RaffleResponse::name)
                .containsExactly("Rifa do Joao Pedro");
        assertThat(raffleService.findAll("joao", RaffleStatus.FINISHED)).isEmpty();
    }

    private void create(String name, String description) {
        raffleService.createRaffle(new RaffleCreate(
                name,
                description,
                BigDecimal.TEN,
                10L,
                LocalDateTime.now().plusDays(7),
                List.of(new PrizeCreate("Primeiro premio", 1))));
    }
}
