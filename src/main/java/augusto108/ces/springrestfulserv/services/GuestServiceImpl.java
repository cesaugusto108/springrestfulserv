package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.repositories.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GuestServiceImpl implements GuestService {
    private final static String ERROR_MESSAGE = "Guest not found. Id: ";

    private final GuestRepository repository;

    public GuestServiceImpl(GuestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Guest fetchGuest(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(ERROR_MESSAGE + id));
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
    public List<Guest> findByName(Name name) {
        final char firstNameFirstChar = name.getFirstName().toUpperCase().charAt(0);
        final String firstNameSubString = name.getFirstName().substring(1).toLowerCase();
        final String firstName = (firstNameFirstChar + firstNameSubString).trim();

        final char lastNameFirstChar = name.getLastName().toUpperCase().charAt(0);
        final String lastNameSubString = name.getLastName().substring(1).toLowerCase();
        final String lastName = (lastNameFirstChar + lastNameSubString).trim();

        final Name n = new Name(firstName, lastName);

        return repository.findByName(n);
    }

    @Override
    public List<Guest> searchGuests(String search) {
        return repository.searchGuests(search.toLowerCase().trim());
    }

    @Override
    public Guest saveGuest(Guest guest) {
        return repository.save(guest);
    }

    @Override
    public void deleteGuest(Long id) {
        Guest guest = fetchGuest(id);

        if (guest == null)
            throw new GuestNotFoundException(ERROR_MESSAGE + id);
        else
            repository.delete(guest);
    }

    @Override
    public Guest checkIn(Guest guest) {
        guest.setStay(Stay.CHECKED_IN);

        return repository.save(guest);
    }

    @Override
    public Guest checkOut(Guest guest) {
        guest.setStay(Stay.CHECKED_OUT);

        return repository.save(guest);
    }

    @Override
    public Guest cancelReserve(Guest guest) {
        guest.setStay(Stay.CANCELLED);

        return repository.save(guest);
    }
}
