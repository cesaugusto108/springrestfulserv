package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Guest Tracker", description = "manages information about hotel guests")
@RequestMapping("/v1/guests")
public interface GuestController {

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "fetch guest by id")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<EntityModel<GuestDto>> fetchGuest(@PathVariable Long id);

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "fetch all guests")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<CollectionModel<EntityModel<GuestDto>>> fetchAllGuests();

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find guest by name")
    @GetMapping(value = "/name-search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<CollectionModel<EntityModel<GuestDto>>> findGuestByName(
            @RequestParam(defaultValue = "", value = "first", required = false) String firstName,
            @RequestParam(defaultValue = "", value = "last", required = false) String lastName
    );

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "search guests")
    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<CollectionModel<EntityModel<GuestDto>>> searchGuests(
            @RequestParam(defaultValue = "", value = "search", required = false) String search
    );

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "save guest")
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<EntityModel<GuestDto>> saveGuest(@RequestBody Guest guest);

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update guest")
    @PutMapping(
            value = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<EntityModel<GuestDto>> updateGuest(@PathVariable("id") Long id, @RequestBody Guest guest);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete guest")
    @DeleteMapping(value = "/{id}")
    ResponseEntity<EntityModel<Link>> deleteGuest(@PathVariable Long id);

    @Operation(summary = "guest check in")
    @PatchMapping(value = "/{id}/check-in",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<?> checkIn(@PathVariable Long id);

    @Operation(summary = "guest check out")
    @PatchMapping(value = "/{id}/check-out",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<?> checkOut(@PathVariable Long id);

    @Operation(summary = "cancel guest's reserve")
    @PatchMapping(value = "/{id}/cancel",produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    ResponseEntity<?> cancelReserve(@PathVariable Long id);
}
