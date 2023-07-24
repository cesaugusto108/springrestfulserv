package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.entities.Address;
import augusto108.ces.springrestfulserv.entities.Guest;
import augusto108.ces.springrestfulserv.entities.Name;
import augusto108.ces.springrestfulserv.entities.enums.Stay;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class GuestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private GuestController guestController;

    @BeforeEach
    void setUp() {
        final String query1 = "insert into `tb_guest` (`id`, `guest_address_city`, `guest_address_number`, `guest_address_street`, " +
                "`guest_email`, `guest_email_domain_name`, `guest_email_username`, `first_name`, `last_name`, `stay`, `guest_telephone`) " +
                "values (1000, 'Aracaju', 321, 'Rua Porto da Folha', 'katia@email.com', '@email.com', 'katia', 'Kátia', 'Moura', 'RESERVED', '79988712340');";

        final String query2 = "insert into `tb_guest` (`id`, `guest_address_city`, `guest_address_number`, `guest_address_street`, " +
                "`guest_email`, `guest_email_domain_name`, `guest_email_username`, `first_name`, `last_name`, `stay`, `guest_telephone`) " +
                "values (1001, 'Aracaju', 897, 'Rua Itabaiana', 'cosme@email.com', '@email.com', 'cosme', 'Cosme', 'Oliveira', 'CHECKED_OUT', '79988712344');";

        final String query3 = "insert into `tb_guest` (`id`, `guest_address_city`, `guest_address_number`, `guest_address_street`, " +
                "`guest_email`, `guest_email_domain_name`, `guest_email_username`, `first_name`, `last_name`, `stay`, `guest_telephone`) " +
                "values (1002, 'Aracaju', 653, 'Rua Lagarto', 'ursula@email.com', '@email.com', 'ursula', 'Úrsula', 'Teles', 'CHECKED_IN', '79988712389');";

        final String query4 = "insert into `tb_guest` (`id`, `guest_address_city`, `guest_address_number`, `guest_address_street`, " +
                "`guest_email`, `guest_email_domain_name`, `guest_email_username`, `first_name`, `last_name`, `stay`, `guest_telephone`) " +
                "values (1003, 'Aracaju', 909, 'Rua Estância', 'penelope@email.com', '@email.com', 'penelope', 'Penelope', 'Teixeira', 'CANCELLED', '79988712311');";

        jdbcTemplate.execute(query1);
        jdbcTemplate.execute(query2);
        jdbcTemplate.execute(query3);
        jdbcTemplate.execute(query4);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("delete from `tb_guest`;");
    }

    @Test
    void fetchGuest() throws Exception {
        mockMvc.perform(get("/v1/guests/{id}?format=json", 1000))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1000)))
                .andExpect(jsonPath("$.stay", is("RESERVED")))
                .andExpect(jsonPath("$.emailAddress.username", is("katia")))
                .andExpect(jsonPath("$.emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests/{id}?format=xml", 1000))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("EntityModel/id").string("1000"))
                .andExpect(xpath("EntityModel/stay").string("RESERVED"))
                .andExpect(xpath("EntityModel/emailAddress/username").string("katia"))
                .andExpect(xpath("EntityModel/emailAddress/domainName").string("@email.com"));

        mockMvc.perform(get("/v1/guests/{id}", 0))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(jsonPath("$.statusCode", is(404)))
                .andExpect(jsonPath("$.message", is("Guest not found. Id: 0")))
                .andExpect(jsonPath("$.error",
                        is("augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException: Guest not found. Id: 0")));

        mockMvc.perform(get("/v1/guests/{id}", "aaa"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", is(400)))
                .andExpect(jsonPath("$.message", is("For input string: \"aaa\"")))
                .andExpect(jsonPath("$.error",
                        is("java.lang.NumberFormatException: For input string: \"aaa\"")));

        assertThrows(NumberFormatException.class, () -> guestController.fetchGuest(Long.parseLong("aaa")));
    }

    @Test
    void fetchAllGuests() throws Exception {
        mockMvc.perform(get("/v1/guests?format=json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList", hasSize(4)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1000)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].stay", is("RESERVED")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.username", is("katia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests?format=xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("CollectionModel/links/rel").string("self"))
                .andExpect(xpath("CollectionModel/content/content/id").string("1000"))
                .andExpect(xpath("CollectionModel/content/content/name/firstName").string("Kátia"));

        mockMvc.perform(get("/v1/guests?format=yml"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.status", is("NOT_ACCEPTABLE")))
                .andExpect(jsonPath("$.statusCode", is(406)))
                .andExpect(jsonPath("$.message",
                        is("Could not find acceptable representation. Acceptable formats: application/xml and application/json")))
                .andExpect(jsonPath("$.error",
                        is("org.springframework.web.HttpMediaTypeNotAcceptableException: Could not find acceptable representation")));
    }

    @Test
    void findGuestByName() throws Exception {
        mockMvc.perform(get("/v1/guests/name-search?format=json")
                        .param("firstName", "kátia")
                        .param("lastName", "moura"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1000)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].name.firstName", is("Kátia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].stay", is("RESERVED")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.username", is("katia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests/name-search?format=xml")
                        .param("firstName", "kátia")
                        .param("lastName", "moura"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("CollectionModel/links/rel").string("self"))
                .andExpect(xpath("CollectionModel/content/content/id").string("1000"))
                .andExpect(xpath("CollectionModel/content/content/name/firstName").string("Kátia"))
                .andExpect(xpath("CollectionModel/content/content/stay").string("RESERVED"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/username").string("katia"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/domainName").string("@email.com"));
    }

    @Test
    void searchGuests() throws Exception {
        mockMvc.perform(get("/v1/guests/search?format=json").param("search", "RESERVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1000)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].name.firstName", is("Kátia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].stay", is("RESERVED")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.username", is("katia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests/search?format=xml").param("search", "RESERVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("CollectionModel/links/rel").string("self"))
                .andExpect(xpath("CollectionModel/content/content/id").string("1000"))
                .andExpect(xpath("CollectionModel/content/content/name/firstName").string("Kátia"))
                .andExpect(xpath("CollectionModel/content/content/stay").string("RESERVED"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/username").string("katia"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/domainName").string("@email.com"));
    }

    @Test
    void saveOrUpdateGuest() throws Exception {
        final Guest guest = new Guest();
        guest.setName(new Name("Maria", "Ferreira"));
        guest.setAddress(new Address("Rua Goiás", 231, "Aracaju"));

        mockMvc.perform(post("/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest))
                        .with(csrf()))
                .andExpect(status().isCreated());

        final List<Guest> guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(5, guests.size());
        assertEquals(Stay.RESERVED, guests.get(0).getStay());
        assertEquals(1, guests.get(0).getId());
        assertEquals("Ferreira", guests.get(0).getName().getLastName());
    }

    @Test
    void deleteGuest() throws Exception {
        List<Guest> guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(1000, guests.get(0).getId());

        mockMvc.perform(delete("/v1/guests/{id}", 1000).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(3, guests.size());
    }

    @Test
    void checkIn() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/check-in", 1000).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<Guest> guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(Stay.CHECKED_IN, guests.get(0).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/check-in", 1001).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot check in guest with stay status CHECKED_OUT")));

        assertEquals(Stay.CHECKED_OUT, guests.get(1).getStay());
    }

    @Test
    void checkOut() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/check-out", 1002).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<Guest> guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(Stay.CHECKED_OUT, guests.get(2).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/check-out", 1000).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot check out guest with stay status RESERVED")));

        assertEquals(Stay.RESERVED, guests.get(0).getStay());
    }

    @Test
    void cancelReserve() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/cancel", 1000).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<Guest> guests = entityManager.createQuery("from Guest order by id", Guest.class).getResultList();
        assertEquals(Stay.CANCELLED, guests.get(0).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/cancel", 1003).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot cancel a reserve of a guest with stay status CANCELLED")));
    }
}