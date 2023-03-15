package augusto108.ces.springrestfulserv.repositories;

import augusto108.ces.springrestfulserv.model.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
