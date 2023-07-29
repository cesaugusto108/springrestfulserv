package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.TestContainersConfiguration;
import augusto108.ces.springrestfulserv.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.entities.*;
import augusto108.ces.springrestfulserv.entities.enums.Stay;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GuestServiceImplTest extends TestContainersConfiguration {
    @Autowired
    private GuestService guestService;

    @Test
    @Order(1)
    void fetchGuest() {
        final GuestDto guestDto = guestService.fetchGuest(10L);

        assertEquals("RESERVED", guestDto.getStay().toString());
        assertEquals("Joice", guestDto.getName().getFirstName());
        assertEquals("Mendes", guestDto.getName().getLastName());
        assertEquals("joice@email.com", guestDto.getEmailAddress().toString());
    }

    @Test
    @Order(2)
    void fetchGuests() {
        final List<GuestDto> guestDtoList = guestService.fetchGuests();

        assertEquals(11, guestDtoList.size());
        assertEquals("CHECKED_OUT", guestDtoList.get(2).getStay().toString());
        assertEquals(3, guestDtoList.get(2).getId(), "Id should be 3");
    }

    @Test
    @Order(3)
    void findByName() {
        final Guest guest = guestService.findGuestById(10L);
        final List<GuestDto> guestDtoList = guestService.findByName(guest.getName());

        assertEquals(1, guestDtoList.size());
        assertEquals("Mendes", guestDtoList.get(0).getName().getLastName());
        assertEquals("RESERVED", guestDtoList.get(0).getStay().toString());
    }

    @Test
    @Order(4)
    void searchGuests() {
        final List<GuestDto> guestDtoList = guestService.searchGuests("Mendes");

        assertEquals(1, guestDtoList.size());
        assertEquals("Mendes", guestDtoList.get(0).getName().getLastName());
        assertEquals("RESERVED", guestDtoList.get(0).getStay().toString());
    }

    @Test
    @Order(10)
    void saveGuest() {
        final Guest guest = new Guest(
                new Name("Marcela", "Carvalho"),
                new Address("Avenida Augusto Franco", 142, "Aracaju"),
                new Telephone("79999999999"),
                "marcela@email.com",
                new EmailAddress("marcela", "@email.com"),
                Stay.RESERVED);

        guestService.saveGuest(guest);

        final List<GuestDto> guests = guestService.fetchGuests();

        assertEquals(12, guests.size());
        assertEquals(Stay.RESERVED, guests.get(11).getStay());
        assertEquals("marcela@email.com", guests.get(11).getEmailAddress().toString());
        assertEquals("79999999999", guests.get(11).getTelephone().toString());
    }

    @Test
    @Order(9)
    void deleteGuest() {
        final Guest guest = new Guest(
                new Name("Leandro", "Souza"),
                new Address("Avenida Ivo do Prado", 142, "Aracaju"),
                new Telephone("79999990000"),
                "leandro@email.com",
                new EmailAddress("leandro", "@email.com"),
                Stay.RESERVED);

        guestService.saveGuest(guest);

        final GuestDto guestToDelete = guestService.searchGuests("Souza").get(0);

        guestService.deleteGuest(guestToDelete.getId());

        final List<GuestDto> guests = guestService.fetchGuests();

        assertEquals(11, guests.size());
        assertEquals(0, guestService.searchGuests("Souza").size());
    }

    @Test
    @Order(5)
    void findGuestById() {
        final Guest guest = guestService.findGuestById(2L);

        assertEquals("rita@email.com", guest.getEmail());
    }

    @Test
    @Order(6)
    void checkIn() {
        Guest guest = guestService.findGuestById(2L);

        guestService.checkIn(guest);

        guest = guestService.findGuestById(2L);

        assertEquals("CHECKED_IN", guest.getStay().toString());
    }

    @Test
    @Order(7)
    void checkOut() {
        Guest guest = guestService.findGuestById(11L);

        guestService.checkOut(guest);

        guest = guestService.findGuestById(11L);

        assertEquals("CHECKED_OUT", guest.getStay().toString());
    }

    @Test
    @Order(8)
    void cancelReserve() {
        Guest guest = guestService.findGuestById(9L);

        guestService.cancelReserve(guest);

        guest = guestService.findGuestById(9L);

        assertEquals("CANCELLED", guest.getStay().toString());
    }
}
