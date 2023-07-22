package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.controllers.helpers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Guest Tracker", description = "manages information about hotel guests")
@RestController
@RequestMapping("/v1/guests")
public class GuestController {
    private final static String METHOD_NOT_ALLOWED = "405 Method not allowed";

    private final GuestService service;
    private final GuestModelAssembler assembler;

    public GuestController(GuestService service, GuestModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "fetch guest by id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<Guest> fetchGuest(@PathVariable Long id) {
        try {
            return assembler.toModel(service.fetchGuest(id));
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    @Operation(summary = "fetch all guests")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> fetchAllGuests() {
        final List<EntityModel<Guest>> guestEntityModels = service.fetchGuests()
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel(),
                linkTo(methodOn(GuestController.class).searchGuests("")).withRel("search"),
                linkTo(methodOn(GuestController.class).findGuestByName(" ", " ")).withRel("name-search")
        );
    }

    @Operation(summary = "find guest by name")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/name-search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> findGuestByName(
            @RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName
    ) {
        final List<EntityModel<Guest>> guestEntityModels = service.findByName(new Name(firstName, lastName))
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).findGuestByName(firstName, lastName)).withSelfRel()
        );
    }

    @Operation(summary = "search guests")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> searchGuests(@RequestParam(defaultValue = "") String search) {
        final List<EntityModel<Guest>> guestEntityModels = service.searchGuests(search)
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).searchGuests(search)).withSelfRel()
        );
    }

    @Operation(summary = "save or update guest")
    @RequestMapping(
            method = {RequestMethod.POST, RequestMethod.PUT},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EntityModel<Guest>> saveOrUpdateGuest(@RequestBody Guest guest) {
        final EntityModel<Guest> entityModel = getSaveOrUpdateGuestEntityModel(guest);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Operation(summary = "delete guest")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<Link> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);

        return EntityModel.of(linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel());
    }

    @Operation(summary = "guest check in")
    @PatchMapping(
            value = "/{id}/check-in",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        final Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail("Cannot check in guest with stay status " + guest.getStay());

        if (guest.getStay() == Stay.RESERVED) return ResponseEntity.ok(assembler.toModel(service.checkIn(guest)));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    @Operation(summary = "guest check out")
    @PatchMapping(
            value = "/{id}/check-out",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        final Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail("Cannot check out guest with stay status " + guest.getStay());

        if (guest.getStay() == Stay.CHECKED_IN) return ResponseEntity.ok(assembler.toModel(service.checkOut(guest)));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    @Operation(summary = "cancel guest's reserve")
    @PatchMapping(
            value = "/{id}/cancel",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> cancelReserve(@PathVariable Long id) {
        final Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail("Cannot cancel a reserve of a guest with stay status " + guest.getStay());

        if (guest.getStay() == Stay.RESERVED) return ResponseEntity.ok(assembler.toModel(service.cancelReserve(guest)));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    private EntityModel<Guest> getSaveOrUpdateGuestEntityModel(Guest guest) {
        final EntityModel<Guest> entityModel;
        final Guest g;

        if (guest.getId() != null) {
            g = service.fetchGuest(guest.getId());

            BeanUtils.copyProperties(guest, g);

            entityModel = assembler.toModel(service.saveGuest(g)); // update guest
        } else {
            guest.setStay(Stay.RESERVED);

            entityModel = assembler.toModel(service.saveGuest(guest)); // save new guest
        }

        return entityModel;
    }
}
