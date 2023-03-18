package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.controllers.helpers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<EntityModel<Guest>> guestEntityModels = service.fetchGuests()
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel());
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
            g.setStay(guest.getStay());

            entityModel = assembler.toModel(service.saveGuest(g));
        } else {
            guest.setStay(Stay.RESERVE);

            entityModel = assembler.toModel(service.saveGuest(guest));
        }

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public EntityModel<Link> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);

        return EntityModel
                .of(linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel());
    }
}
