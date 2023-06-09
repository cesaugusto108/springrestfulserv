package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.controllers.helpers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.Name;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
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

@RestController
@RequestMapping("/guests")
public class GuestController {
    private final static String METHOD_NOT_ALLOWED = "405 Method not allowed";

    private final GuestService service;
    private final GuestModelAssembler assembler;

    public GuestController(GuestService service, GuestModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<Guest> fetchGuest(@PathVariable Long id) {
        try {
            return assembler.toModel(service.fetchGuest(id));
        } catch (NumberFormatException e) {
            throw new NumberFormatException(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> fetchAllGuests() {
        List<EntityModel<Guest>> guestEntityModels = service.fetchGuests()
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/name-search", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> findGuestByName(
            @RequestParam(defaultValue = "") String firstName, @RequestParam(defaultValue = "") String lastName
    ) {
        List<EntityModel<Guest>> guestEntityModels = service.findByName(new Name(firstName, lastName))
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).findGuestByName(firstName, lastName)).withSelfRel()
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/search", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public CollectionModel<EntityModel<Guest>> searchGuests(@RequestParam(defaultValue = "") String search) {
        List<EntityModel<Guest>> guestEntityModels = service.searchGuests(search)
                .stream()
                .map(guest -> assembler.toModel(guest))
                .collect(Collectors.toList());

        return CollectionModel.of(
                guestEntityModels,
                linkTo(methodOn(GuestController.class).searchGuests(search)).withSelfRel()
        );
    }

    @RequestMapping(
            method = {RequestMethod.POST, RequestMethod.PUT},
            produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EntityModel<Guest>> saveOrUpdateGuest(@RequestBody Guest guest) {
        EntityModel<Guest> entityModel = getGuestEntityModelSaveOrUpdate(guest);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public EntityModel<Link> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);

        return EntityModel
                .of(linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel());
    }

    @PatchMapping(
            value = "/{id}/check-in",
            produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail("Cannot check in guest with stay status " + guest.getStay());

        if (guest.getStay() == Stay.RESERVED)
            return ResponseEntity.ok(assembler.toModel(service.checkIn(guest)));


        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    @PatchMapping(
            value = "/{id}/check-out",
            produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail("Cannot check out guest with stay status " + guest.getStay());

        if (guest.getStay() == Stay.CHECKED_IN)
            return ResponseEntity.ok(assembler.toModel(service.checkOut(guest)));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    @PatchMapping(
            value = "/{id}/cancel",
            produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> cancelReserve(@PathVariable Long id) {
        Guest guest = service.fetchGuest(id);

        final Problem problem = Problem.create()
                .withTitle(METHOD_NOT_ALLOWED)
                .withDetail(
                        "Cannot cancel a reserve of a guest with stay status " + guest.getStay()
                );

        if (guest.getStay() == Stay.RESERVED)
            return ResponseEntity.ok(assembler.toModel(service.cancelReserve(guest)));

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(problem);
    }

    private EntityModel<Guest> getGuestEntityModelSaveOrUpdate(Guest guest) {
        EntityModel<Guest> entityModel;
        Guest g;

        if (guest.getId() != null) {
            g = service.fetchGuest(guest.getId());

            g.setName(guest.getName());
            g.setAddress(guest.getAddress());
            g.setTelephone(guest.getTelephone());
            g.setEmail(guest.getEmail());
            g.setEmailAddress(guest.getEmailAddress());
            g.setStay(guest.getStay());

            entityModel = assembler.toModel(service.saveGuest(g)); // update guest
        } else {
            guest.setStay(Stay.RESERVED);

            entityModel = assembler.toModel(service.saveGuest(guest)); // save new guest
        }

        return entityModel;
    }
}
