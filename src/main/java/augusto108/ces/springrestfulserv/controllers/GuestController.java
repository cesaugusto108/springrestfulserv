package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/guests")
public class GuestController {
    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public EntityModel<Guest> fetchGuest(@PathVariable Long id) {
        return EntityModel.of(
                service.fetchGuest(id),
                linkTo(methodOn(GuestController.class).fetchGuest(id)).withSelfRel(),
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withRel("guests")
        );
    }

    // this will return an aggregate root resource
    @GetMapping("/all")
    public CollectionModel<EntityModel<Guest>> fetchAllGuests() {
        List<EntityModel<Guest>> guests = service.fetchGuests()
                .stream()
                .map(guest -> EntityModel.of(
                                guest,
                                linkTo(methodOn(GuestController.class).fetchGuest(guest.getId()))
                                        .withSelfRel(),
                                linkTo(methodOn(GuestController.class).fetchAllGuests())
                                        .withRel("guests")
                        )
                )
                .collect(Collectors.toList());

        return CollectionModel.of(
                guests,
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel()
        );
    }

    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> saveGuest(@RequestBody Guest guest) {
        service.saveGuest(guest);

        return ResponseEntity.noContent().build();
    }
}
