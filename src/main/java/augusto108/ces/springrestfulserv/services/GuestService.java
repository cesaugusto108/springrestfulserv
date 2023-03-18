package augusto108.ces.springrestfulserv.services;

import java.util.List;

import augusto108.ces.springrestfulserv.model.Guest;

public interface GuestService {
    Guest fetchGuest(Long id);

    List<Guest> fetchGuests();

    Guest saveGuest(Guest guest);

    void deleteGuest(Long id);

    Guest checkIn(Guest guest);

    Guest checkOut(Guest guest);

    Guest cancelReserve(Guest guest);
}
