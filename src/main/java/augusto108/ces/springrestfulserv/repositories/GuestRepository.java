package augusto108.ces.springrestfulserv.repositories;

import augusto108.ces.springrestfulserv.model.datatypes.Name;
import augusto108.ces.springrestfulserv.model.entities.Guest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuestRepository extends CrudRepository<Guest, Long> {

    List<Guest> findByName(Name name);

    @Query("from Guest g where " +
            "lower(g.name.firstName) like lower(concat('%', :search, '%')) or " +
            "lower(g.name.lastName) like lower(concat('%', :search, '%')) or " +
            "lower(g.telephone) like lower(concat('%', :search, '%')) or " +
            "lower(g.email) like lower(concat('%', :search, '%')) or " +
            "lower(g.stay) like lower(concat('%', :search, '%'))")
    List<Guest> searchGuests(@Param("search") String search);
}
