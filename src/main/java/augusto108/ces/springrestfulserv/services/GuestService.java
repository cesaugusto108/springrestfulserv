package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;

import java.util.List;

public interface GuestService {
    Guest fetchGuest(Long id);

    List<Guest> fetchGuests();

    List<Guest> findByName(Name name);

    List<Guest> searchGuests(String search);

    Guest saveGuest(Guest guest);

    void deleteGuest(Long id);

    Guest checkIn(Guest guest);

    Guest checkOut(Guest guest);

    Guest cancelReserve(Guest guest);
}
