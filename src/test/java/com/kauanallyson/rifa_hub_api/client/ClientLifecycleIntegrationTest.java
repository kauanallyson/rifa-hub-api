package com.kauanallyson.rifa_hub_api.client;

import com.kauanallyson.rifa_hub_api.client.dtos.ClientCreate;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientResponse;
import com.kauanallyson.rifa_hub_api.client.dtos.ClientUpdate;
import com.kauanallyson.rifa_hub_api.shared.exceptions.DuplicateResourceException;
import com.kauanallyson.rifa_hub_api.shared.exceptions.ResourceNotFoundException;
import com.kauanallyson.rifa_hub_api.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class ClientLifecycleIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void clearClients() {
        clientRepository.deleteAll();
    }

    @Test
    void softDeletedClientDisappearsFromEveryRead() {
        ClientResponse joao = clientService.createClient(
                new ClientCreate("Joao Pedro", "11999990001", "joao@example.com"));

        assertThat(clientService.findAll(null))
                .extracting(ClientResponse::name)
                .contains("Joao Pedro");

        clientService.deleteClient(joao.id());

        assertThat(clientService.findAll(null))
                .extracting(ClientResponse::name)
                .doesNotContain("Joao Pedro");

        assertThatThrownBy(() -> clientService.findClientById(joao.id()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void softDeletedClientKeepsItsRowAndItsEmail() {
        ClientResponse maria = clientService.createClient(
                new ClientCreate("Maria Silva", "11999990002", "maria@example.com"));
        clientService.deleteClient(maria.id());

        assertThat(clientRepository.findById(maria.id())).isPresent();

        assertThatThrownBy(() -> clientService.createClient(
                new ClientCreate("Maria Souza", "11999990003", "maria@example.com")))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void duplicateEmailAndPhoneAreRejected() {
        clientService.createClient(new ClientCreate("Pedro Henrique", "11999990004", "pedro@example.com"));

        assertThatThrownBy(() -> clientService.createClient(
                new ClientCreate("Pedro Alves", "11999990005", "pedro@example.com")))
                .isInstanceOf(DuplicateResourceException.class);

        assertThatThrownBy(() -> clientService.createClient(
                new ClientCreate("Pedro Lima", "11999990004", "outro@example.com")))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void nameSearchIgnoresCaseAndMatchesAnyWord() {
        clientService.createClient(new ClientCreate("Ana Clara", "11999990006", "ana@example.com"));

        assertThat(clientService.findAll("Ana Clara"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");
        assertThat(clientService.findAll("ana clara"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");
        assertThat(clientService.findAll("ANA CLARA"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");

        assertThat(clientService.findAll("ana"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");
        assertThat(clientService.findAll("clara"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");
        assertThat(clientService.findAll("CLARA"))
                .extracting(ClientResponse::name)
                .containsExactly("Ana Clara");
    }

    @Test
    void nameSearchRequiresEveryWordAndIgnoresNonMatches() {
        clientService.createClient(new ClientCreate("Joao Pedro Silva", "11999990008", "jp@example.com"));
        clientService.createClient(new ClientCreate("Maria Silva", "11999990009", "ms@example.com"));

        assertThat(clientService.findAll("silva"))
                .extracting(ClientResponse::name)
                .containsExactlyInAnyOrder("Joao Pedro Silva", "Maria Silva");

        assertThat(clientService.findAll("pedro silva"))
                .extracting(ClientResponse::name)
                .containsExactly("Joao Pedro Silva");

        assertThat(clientService.findAll("carlos")).isEmpty();
    }

    @Test
    void blankSearchReturnsEveryActiveClient() {
        clientService.createClient(new ClientCreate("Ana Clara", "11999990010", "ana2@example.com"));
        clientService.createClient(new ClientCreate("Maria Silva", "11999990011", "maria2@example.com"));

        assertThat(clientService.findAll(null)).hasSize(2);
        assertThat(clientService.findAll("   ")).hasSize(2);
    }

    // Records current behavior: ClientService lost the null guard that SellerService kept,
    // so this reaches the global handler as a 500.
    @Test
    void updatingWithoutAnEmailCurrentlyThrowsNpe() {
        ClientResponse lucas = clientService.createClient(
                new ClientCreate("Lucas Gabriel", "11999990007", "lucas@example.com"));

        Throwable thrown = catchThrowable(() -> clientService.updateClient(
                lucas.id(), new ClientUpdate("Lucas G", "11999990007", null)));

        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }
}
