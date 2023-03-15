package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.repositories.GuestRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl implements GuestService {
    private final GuestRepository repository;

    public GuestServiceImpl(GuestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Guest saveGuest(Guest guest) {
        return repository.save(guest);
    }

    @Override
    public Guest fetchGuest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException("Guest not found. Id: " + id));
    }

    @Override
    public List<Guest> fetchGuests() {
        Iterable<Guest> guests = repository.findAll();

        List<Guest> guestList = new ArrayList<>();

        for (Guest guest : guests) {
            guestList.add(guest);
        }

        return guestList;
    }

    @Override
    public void deleteGuest(Long id) {
        Guest guest = fetchGuest(id);

        if (guest == null)
            throw new GuestNotFoundException("Guest not found. Id: " + id);
        else
            repository.delete(guest);
    }
}
