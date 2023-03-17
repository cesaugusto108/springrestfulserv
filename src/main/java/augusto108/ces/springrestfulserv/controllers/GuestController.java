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

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<EntityModel<Guest>> saveOrUpdateGuest(@RequestBody Guest guest) {
        Guest g = null;
        EntityModel<Guest> entityModel = null;

        if (guest.getId() != null) {
            g = service.fetchGuest(guest.getId());

            g.setName(guest.getName());
            g.setAddress(guest.getAddress());
            g.setTelephone(guest.getTelephone());
            g.setEmail(guest.getEmail());

            entityModel = assembler.toModel(service.saveGuest(g));
        } else entityModel = assembler.toModel(service.saveGuest(guest));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);

        return ResponseEntity.noContent().build();
    }
}
