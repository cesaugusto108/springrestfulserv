package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.model.datatypes.Name;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;

import java.util.List;

public interface GuestService {

    GuestDto fetchGuest(Long id);

    List<GuestDto> fetchGuests();

    List<GuestDto> findByName(Name name);

    Guest findGuestById(Long id);

    List<GuestDto> searchGuests(String search);

    GuestDto saveGuest(Guest guest);

    void deleteGuest(Long id);

    GuestDto checkIn(Guest guest);

    GuestDto checkOut(Guest guest);

    GuestDto cancelReserve(Guest guest);
}
