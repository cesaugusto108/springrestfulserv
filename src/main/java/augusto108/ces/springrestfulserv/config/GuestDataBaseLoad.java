package augusto108.ces.springrestfulserv.config;

import augusto108.ces.springrestfulserv.model.*;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.logging.Logger;

@Configuration
@Profile("!test")
public class GuestDataBaseLoad {
    private static final Logger LOGGER = Logger.getLogger(GuestDataBaseLoad.class.getName());

    private final GuestService service;

    public GuestDataBaseLoad(GuestService service) {
        this.service = service;
    }

    @Bean
    CommandLineRunner initDatabase() {
        final Guest g1 = new Guest(
                new Name("Marcela", "Carvalho"),
                new Address("Av. Augusto Franco", 142, "Aracaju"),
                new Telephone("79999999999"),
                "marcela@email.com",
                new EmailAddress("marcela", "@email.com"),
                Stay.RESERVED
        );

        final Guest g2 = new Guest(
                new Name("João Carlos", "Souza"),
                new Address("Rua Boquim", 552, "Aracaju"),
                new Telephone("79998989898"),
                "joaocarlos@email.com",
                new EmailAddress("joaocarlos", "email.com"),
                Stay.CHECKED_IN
        );

        return args -> {
            LOGGER.info("-> -> Database load: " + service.saveGuest(g1));
            LOGGER.info("-> -> Database load: " + service.saveGuest(g2));
        };
    }
}
