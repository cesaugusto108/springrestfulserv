package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.assemblers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;
import augusto108.ces.springrestfulserv.model.entities.Name;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
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

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "fetch guest by id")
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EntityModel<GuestDto>> fetchGuest(@PathVariable Long id) {
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.fetchGuest(id));
        final URI uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
        return ResponseEntity.status(200).location(uri).body(entityModel);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "fetch all guests")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> fetchAllGuests() {
        final List<EntityModel<GuestDto>> guestEntityModels = service.fetchGuests()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link fetchAllGuestsLink = linkTo(methodOn(GuestController.class).fetchAllGuests()).withSelfRel();
        final Link searchGuestsLink = linkTo(methodOn(GuestController.class).searchGuests("")).withRel("search");
        final Link findGuestByNameLink = linkTo(methodOn(GuestController.class).findGuestByName(" ", " ")).withRel("name-search");
        final List<Link> links = Arrays.asList(fetchAllGuestsLink, searchGuestsLink, findGuestByNameLink);
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, links);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.status(200).location(uri).body(collectionModel);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "find guest by name")
    @GetMapping(value = "/name-search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> findGuestByName(
            @RequestParam(defaultValue = "", value = "first", required = false) String firstName,
            @RequestParam(defaultValue = "", value = "last", required = false) String lastName
    ) {
        final List<EntityModel<GuestDto>> guestEntityModels = service.findByName(new Name(firstName, lastName))
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link findGuestByNameLink = linkTo(methodOn(GuestController.class).findGuestByName(firstName, lastName)).withSelfRel();
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, findGuestByNameLink);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.status(200).location(uri).body(collectionModel);
    }

    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "search guests")
    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> searchGuests(
            @RequestParam(defaultValue = "", value = "search", required = false) String search
    ) {
        final List<EntityModel<GuestDto>> guestEntityModels = service.searchGuests(search)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link searchGuestsLink = linkTo(methodOn(GuestController.class).searchGuests(search)).withSelfRel();
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, searchGuestsLink);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.status(200).location(uri).body(collectionModel);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "save or update guest")
    @RequestMapping(
            method = {RequestMethod.POST, RequestMethod.PUT},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public ResponseEntity<EntityModel<GuestDto>> saveOrUpdateGuest(@RequestBody Guest guest) {
        final EntityModel<GuestDto> entityModel = getSavedOrUpdatedGuestEntityModel(guest);
        final URI uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
        return ResponseEntity.status(201).location(uri).body(entityModel);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "delete guest")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<EntityModel<Link>> deleteGuest(@PathVariable Long id) {
        service.deleteGuest(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "guest check in")
    @PatchMapping(
            value = "/{id}/check-in",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.checkIn(guest));
        final URI uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
        if (guestDto.getStay() == Stay.RESERVED) return ResponseEntity.status(200).location(uri).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot check in guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).location(uri).header(contentType, problemDetailsJsonValue).body(problem);
    }

    @Operation(summary = "guest check out")
    @PatchMapping(
            value = "/{id}/check-out",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.checkOut(guest));
        final URI uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
        if (guestDto.getStay() == Stay.CHECKED_IN) return ResponseEntity.status(200).location(uri).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot check out guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).location(uri).header(contentType, problemDetailsJsonValue).body(problem);
    }

    @Operation(summary = "cancel guest's reserve")
    @PatchMapping(
            value = "/{id}/cancel",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> cancelReserve(@PathVariable Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.cancelReserve(guest));
        final URI uri = entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri();
        if (guestDto.getStay() == Stay.RESERVED) return ResponseEntity.status(200).location(uri).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot cancel a reserve of a guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).location(uri).header(contentType, problemDetailsJsonValue).body(problem);
    }

    private EntityModel<GuestDto> getSavedOrUpdatedGuestEntityModel(Guest guest) {
        final EntityModel<GuestDto> entityModel;
        final GuestDto guestDto;

        if (guest.getId() != null) {
            guestDto = service.fetchGuest(guest.getId());
            BeanUtils.copyProperties(guest, guestDto, "id");
            final Guest g = service.findGuestById(guestDto.getId());
            entityModel = assembler.toModel(service.saveGuest(g)); // update guest
        } else {
            guest.setStay(Stay.RESERVED);
            entityModel = assembler.toModel(service.saveGuest(guest)); // save new guest
        }

        return entityModel;
    }
}
