package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.controllers.helpers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guests")
public class GuestController {
    private final GuestService service;
    private final GuestModelAssembler assembler;

    public GuestController(GuestService service, GuestModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public EntityModel<Guest> fetchGuest(@PathVariable Long id) {
        return assembler.toModel(service.fetchGuest(id));
    }

    // this will return an aggregate root resource
    @GetMapping
    public CollectionModel<EntityModel<Guest>> fetchAllGuests() {
        return assembler.toCollectionModel(service.fetchGuests());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Guest>> saveGuest(@RequestBody Guest guest) {
        return getEntityModelResponseEntity(guest);
    }

    @PutMapping
    public ResponseEntity<EntityModel<Guest>> updateGuest(@RequestBody Guest guest) {
        Guest g = service.fetchGuest(guest.getId());

        g.setName(guest.getName());
        g.setAddress(guest.getAddress());
        g.setTelephone(guest.getTelephone());
        g.setEmail(guest.getEmail());

        return getEntityModelResponseEntity(g);
    }

    @DeleteMapping("/{id}")
    public CollectionModel<EntityModel<Guest>> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);

        return assembler.toCollectionModel(service.fetchGuests());
    }

    private ResponseEntity<EntityModel<Guest>> getEntityModelResponseEntity(Guest g) {
        EntityModel<Guest> entityModel = assembler.toModel(service.saveGuest(g));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
