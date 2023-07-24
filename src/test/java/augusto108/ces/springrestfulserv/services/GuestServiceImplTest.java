package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.entities.*;
import augusto108.ces.springrestfulserv.entities.enums.Stay;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class GuestServiceImplTest {
    @Autowired
    private GuestService guestService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        final String query = "insert into `tb_guest` (`id`, `guest_address_city`, `guest_address_number`, `guest_address_street`, " +
                "`guest_email`, `guest_email_domain_name`, `guest_email_username`, `first_name`, `last_name`, `stay`, `guest_telephone`) " +
                "values (1000, 'Aracaju', 321, 'Rua Porto da Folha', 'katia@email.com', '@email.com', 'katia', 'Kátia', 'Moura', 'RESERVED', '79988712340');";

        jdbcTemplate.execute(query);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("delete from tb_guest;");
    }

    @Test
    void fetchGuest() {
        final GuestDto guestDto = guestService.fetchGuest(1000L);

        assertEquals("RESERVED", guestDto.getStay().toString());
        assertEquals("Kátia", guestDto.getName().getFirstName());
        assertEquals("Moura", guestDto.getName().getLastName());
        assertEquals("katia@email.com", guestDto.getEmailAddress().toString());
    }

    @Test
    void fetchGuests() {
        final List<GuestDto> guestDtoList = guestService.fetchGuests();

        assertEquals(1, guestDtoList.size());
        assertEquals("RESERVED", guestDtoList.get(0).getStay().toString());
        assertEquals(1000, guestDtoList.get(0).getId(), "Id should be 1000");
    }

    @Test
    void findByName() {
        final Guest g = entityManager
                .createQuery("from Guest g where email = 'katia@email.com'", Guest.class)
                .getSingleResult();
        final List<GuestDto> guestDtoList = guestService.findByName(g.getName());

        assertEquals(1, guestDtoList.size());
        assertEquals("Moura", guestDtoList.get(0).getName().getLastName());
        assertEquals("RESERVED", guestDtoList.get(0).getStay().toString());
    }

    @Test
    void searchGuests() {
        final List<GuestDto> guestDtoList = guestService.searchGuests("Moura");

        assertEquals(1, guestDtoList.size());
        assertEquals("Moura", guestDtoList.get(0).getName().getLastName());
        assertEquals("RESERVED", guestDtoList.get(0).getStay().toString());
    }

    @Test
    void saveGuest() {
        final Guest g1 = new Guest(
                new Name("Marcela", "Carvalho"),
                new Address("Av. Augusto Franco", 142, "Aracaju"),
                new Telephone("79999999999"),
                "marcela@email.com",
                new EmailAddress("marcela", "@email.com"),
                Stay.RESERVED);

        final Guest g2 = new Guest(
                new Name("João Carlos", "Souza"),
                new Address("Rua Boquim", 552, "Aracaju"),
                new Telephone("79998989898"),
                "joaocarlos@email.com",
                new EmailAddress("joaocarlos", "email.com"),
                Stay.CHECKED_IN);

        guestService.saveGuest(g1);
        guestService.saveGuest(g2);

        final List<Guest> guests = entityManager
                .createQuery("from Guest order by id", Guest.class)
                .getResultList();

        assertEquals(3, guests.size());
        assertEquals(Stay.RESERVED, guests.get(0).getStay());
        assertEquals("marcela@email.com", guests.get(0).getEmail());
        assertEquals("79999999999", guests.get(0).getTelephone().toString());
        assertEquals(Stay.CHECKED_IN, guests.get(1).getStay());
        assertEquals("joaocarlos@email.com", guests.get(1).getEmail());
        assertEquals("79998989898", guests.get(1).getTelephone().toString());
    }
}
