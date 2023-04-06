package augusto108.ces.springrestfulserv.repositories;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuestRepository extends CrudRepository<Guest, Long> {
    List<Guest> findByName(Name name);

    @Query(
            "from Guest g where " +
                    "lower(first_name) like lower(concat('%', :search, '%')) or " +
                    "lower(last_name) like lower(concat('%', :search, '%')) or " +
                    "lower(guest_telephone) like lower(concat('%', :search, '%')) or " +
                    "lower(guest_email) like lower(concat('%', :search, '%')) or " +
                    "lower(stay) like lower(concat('%', :search, '%'))"
    )
    List<Guest> searchGuests(@Param("search") String search);

}
