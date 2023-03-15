package augusto108.ces.springrestfulserv.services;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.repositories.GuestRepository;
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
}
