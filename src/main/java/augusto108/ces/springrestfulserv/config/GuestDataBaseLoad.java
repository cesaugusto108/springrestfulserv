package augusto108.ces.springrestfulserv.config;

import augusto108.ces.springrestfulserv.model.Address;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;
import augusto108.ces.springrestfulserv.model.Telephone;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class GuestDataBaseLoad {
    private static final Logger LOGGER = Logger.getLogger(GuestDataBaseLoad.class.getName());

    private final GuestService service;

    public GuestDataBaseLoad(GuestService service) {
        this.service = service;
    }

    @Bean
    CommandLineRunner initDatabase() {
        Guest g1 = new Guest(
                new Name("Marcela", "Carvalho"),
                new Address("Av. Augusto Franco", 142, "Aracaju"),
                new Telephone("79999999999"),
                "marcela@email.com",
                Stay.RESERVED
        );

        Guest g2 = new Guest(
                new Name("João Carlos", "Souza"),
                new Address("Rua Boquim", 552, "Aracaju"),
                new Telephone("79998989898"),
                "joaocarlos@email.com",
                Stay.CHECKED_IN
        );

        return args -> {
              LOGGER.info("-> -> Database load: " + service.saveGuest(g1));
              LOGGER.info("-> -> Database load: " + service.saveGuest(g2));
        };
    }
}
