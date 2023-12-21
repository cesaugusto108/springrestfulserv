package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;
import augusto108.ces.springrestfulserv.model.datatypes.Name;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.model.mapper.DtoMapper;
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
    public GuestDto fetchGuest(Long id) {
        final Guest guest = repository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(ERROR_MESSAGE + id));

        return DtoMapper.fromGuestToGuestDto(guest);
    }

    @Override
    public List<GuestDto> fetchGuests() {
        final Iterable<Guest> guests = repository.findAll();

        final List<GuestDto> guestDtoList = new ArrayList<>();

        for (Guest guest : guests) {
            guestDtoList.add(DtoMapper.fromGuestToGuestDto(guest));
        }

        return guestDtoList;
    }

    @Override
    public List<GuestDto> findByName(Name name) {
        final char firstNameFirstChar = name.getFirstName().toUpperCase().charAt(0);
        final String firstNameSubString = name.getFirstName().substring(1).toLowerCase();
        final String firstName = (firstNameFirstChar + firstNameSubString).trim();

        final char lastNameFirstChar = name.getLastName().toUpperCase().charAt(0);
        final String lastNameSubString = name.getLastName().substring(1).toLowerCase();
        final String lastName = (lastNameFirstChar + lastNameSubString).trim();

        final Name n = new Name(firstName, lastName);

        final List<Guest> guestDtoList = repository.findByName(n);

        return DtoMapper.fromGuestListToGuestDtoList(guestDtoList);
    }

    @Override
    public Guest findGuestById(Long id) {
        return repository.findById(id).orElseThrow(() -> new GuestNotFoundException(ERROR_MESSAGE + id));
    }

    @Override
    public List<GuestDto> searchGuests(String search) {
        final List<Guest> guestDtoList = repository.searchGuests(search.toLowerCase().trim());

        return DtoMapper.fromGuestListToGuestDtoList(guestDtoList);
    }

    @Override
    public GuestDto saveGuest(Guest guest) {
        final Guest g = repository.save(guest);

        return DtoMapper.fromGuestToGuestDto(g);
    }

    @Override
    public void deleteGuest(Long id) {
        Guest guest = findGuestById(id);

        repository.delete(guest);
    }

    @Override
    public GuestDto checkIn(Guest guest) {
        guest.setStay(Stay.CHECKED_IN);

        final Guest g = repository.save(guest);

        return DtoMapper.fromGuestToGuestDto(g);
    }

    @Override
    public GuestDto checkOut(Guest guest) {
        guest.setStay(Stay.CHECKED_OUT);

        final Guest g = repository.save(guest);

        return DtoMapper.fromGuestToGuestDto(g);
    }

    @Override
    public GuestDto cancelReserve(Guest guest) {
        guest.setStay(Stay.CANCELLED);

        final Guest g = repository.save(guest);

        return DtoMapper.fromGuestToGuestDto(g);
    }
}
