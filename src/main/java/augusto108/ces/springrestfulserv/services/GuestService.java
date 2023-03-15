package augusto108.ces.springrestfulserv.services;

import java.util.List;

import augusto108.ces.springrestfulserv.model.Guest;

public interface GuestService {
    Guest saveGuest(Guest guest);

    Guest fetchGuest(Long id);

    List<Guest> fetchGuests();

    void deleteGuest(Long id);
}
