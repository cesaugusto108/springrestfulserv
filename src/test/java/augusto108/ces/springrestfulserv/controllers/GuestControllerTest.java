package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.TestContainersConfiguration;
import augusto108.ces.springrestfulserv.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.entities.Address;
import augusto108.ces.springrestfulserv.entities.Guest;
import augusto108.ces.springrestfulserv.entities.Name;
import augusto108.ces.springrestfulserv.entities.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GuestControllerTest extends TestContainersConfiguration {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestController guestController;

    @Autowired
    private GuestService guestService;

    @Test
    @Order(1)
    void fetchGuest() throws Exception {
        mockMvc.perform(get("/v1/guests/{id}?format=json", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.stay", is("RESERVED")))
                .andExpect(jsonPath("$.emailAddress.username", is("katia")))
                .andExpect(jsonPath("$.emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests/{id}?format=xml", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("EntityModel/id").string("1"))
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
    @Order(2)
    void fetchAllGuests() throws Exception {
        mockMvc.perform(get("/v1/guests?format=json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList", hasSize(11)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].stay", is("RESERVED")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.username", is("katia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests?format=xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("CollectionModel/links/rel").string("self"))
                .andExpect(xpath("CollectionModel/content/content/id").string("1"))
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
    @Order(3)
    void findGuestByName() throws Exception {
        mockMvc.perform(get("/v1/guests/name-search?format=json")
                        .param("firstName", "kátia")
                        .param("lastName", "moura"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1)))
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
                .andExpect(xpath("CollectionModel/content/content/id").string("1"))
                .andExpect(xpath("CollectionModel/content/content/name/firstName").string("Kátia"))
                .andExpect(xpath("CollectionModel/content/content/stay").string("RESERVED"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/username").string("katia"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/domainName").string("@email.com"));
    }

    @Test
    @Order(4)
    void searchGuests() throws Exception {
        mockMvc.perform(get("/v1/guests/search?format=json").param("search", "RESERVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].name.firstName", is("Kátia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].stay", is("RESERVED")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.username", is("katia")))
                .andExpect(jsonPath("$._embedded.guestDtoList[0].emailAddress.domainName", is("@email.com")));

        mockMvc.perform(get("/v1/guests/search?format=xml").param("search", "RESERVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/xml"))
                .andExpect(xpath("CollectionModel/links/rel").string("self"))
                .andExpect(xpath("CollectionModel/content/content/id").string("1"))
                .andExpect(xpath("CollectionModel/content/content/name/firstName").string("Kátia"))
                .andExpect(xpath("CollectionModel/content/content/stay").string("RESERVED"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/username").string("katia"))
                .andExpect(xpath("CollectionModel/content/content/emailAddress/domainName").string("@email.com"));
    }

    @Test
    @Order(9)
    void saveOrUpdateGuest() throws Exception {
        final Guest guest = new Guest();
        guest.setName(new Name("Maria", "Ferreira"));
        guest.setAddress(new Address("Rua Goiás", 231, "Aracaju"));

        mockMvc.perform(post("/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest))
                        .with(csrf()))
                .andExpect(status().isCreated());

        final List<GuestDto> guests = guestService.fetchGuests();
        assertEquals(12, guests.size());
        assertEquals("Ferreira", guests.get(11).getName().getLastName());

        guestService.deleteGuest(guests.get(11).getId());
    }

    @Test
    @Order(8)
    void deleteGuest() throws Exception {
        final Guest guest = new Guest();
        guest.setName(new Name("Ana", "Guimarães"));
        guest.setAddress(new Address("Rua Paraíba", 65, "Aracaju"));

        mockMvc.perform(post("/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(guest))
                        .with(csrf()))
                .andExpect(status().isCreated());

        List<GuestDto> guests = guestService.fetchGuests();
        assertEquals(6, guests.get(5).getId());

        mockMvc.perform(delete("/v1/guests/{id}", 6).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        guests = guestService.fetchGuests();
        assertEquals(11, guests.size());
    }

    @Test
    @Order(5)
    void checkIn() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/check-in", 1).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<GuestDto> guests = guestService.fetchGuests();
        assertEquals(Stay.CHECKED_IN, guests.get(0).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/check-in", 3).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot check in guest with stay status CHECKED_OUT")));

        assertEquals(Stay.CHECKED_OUT, guests.get(2).getStay());
    }

    @Test
    @Order(6)
    void checkOut() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/check-out", 5).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<GuestDto> guests = guestService.fetchGuests();
        assertEquals(Stay.CHECKED_OUT, guests.get(2).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/check-out", 9).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot check out guest with stay status RESERVED")));

        assertEquals(Stay.RESERVED, guests.get(8).getStay());
    }

    @Test
    @Order(7)
    void cancelReserve() throws Exception {
        mockMvc.perform(patch("/v1/guests/{id}/cancel", 2).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));

        final List<GuestDto> guests = guestService.fetchGuests();
        assertEquals(Stay.CANCELLED, guests.get(1).getStay());

        mockMvc.perform(patch("/v1/guests/{id}/cancel", 7).with(csrf()))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/problem+json"))
                .andExpect(jsonPath("$.title", is("405 Method not allowed")))
                .andExpect(jsonPath("$.detail", is("Cannot cancel a reserve of a guest with stay status CANCELLED")));
    }
}