package augusto108.ces.springrestfulserv.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.services.GuestService;

@RestController
@RequestMapping("/guests")
public class GuestController {
    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guest> fetchGuest(@PathVariable Long id) {
        return ResponseEntity.ok(service.fetchGuest(id));
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
