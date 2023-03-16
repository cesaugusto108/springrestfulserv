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
    @GetMapping("/all")
    public CollectionModel<EntityModel<Guest>> fetchAllGuests() {
        return assembler.toCollectionModel(service.fetchGuests());
    }

    @PostMapping("/save")
    public ResponseEntity<EntityModel<Guest>> saveGuest(@RequestBody Guest guest) {
        EntityModel<Guest> entityModel = assembler.toModel(service.saveGuest(guest));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }
}
