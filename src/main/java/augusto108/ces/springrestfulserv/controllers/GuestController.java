package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<Guest>> fetchAllGuests() {
        return ResponseEntity.ok(service.fetchGuests());
    }

    @RequestMapping(value = {"/save"}, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Void> saveGuest(@RequestBody Guest guest) {
        service.saveGuest(guest);

        return ResponseEntity.noContent().build();
    }
}
