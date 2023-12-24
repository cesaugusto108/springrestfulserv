package augusto108.ces.springrestfulserv.controllers;

import augusto108.ces.springrestfulserv.assemblers.GuestModelAssembler;
import augusto108.ces.springrestfulserv.exceptions.UnmatchedIdException;
import augusto108.ces.springrestfulserv.model.datatypes.Name;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import augusto108.ces.springrestfulserv.services.GuestService;
import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GuestControllerImpl implements GuestController {

    private final static String METHOD_NOT_ALLOWED = "405 Method not allowed";

    private final GuestService service;
    private final GuestModelAssembler assembler;

    public GuestControllerImpl(GuestService service, GuestModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    public ResponseEntity<EntityModel<GuestDto>> fetchGuest(Long id) {
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.fetchGuest(id));
        return ResponseEntity.status(200).body(entityModel);
    }

    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> fetchAllGuests() {
        final List<EntityModel<GuestDto>> guestEntityModels = service.fetchGuests()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link fetchAllGuestsLink = linkTo(methodOn(GuestControllerImpl.class).fetchAllGuests()).withSelfRel();
        final Link searchGuestsLink = linkTo(methodOn(GuestControllerImpl.class).searchGuests("")).withRel("search");
        final Link findGuestByNameLink = linkTo(methodOn(GuestControllerImpl.class).findGuestByName(" ", " ")).withRel("name-search");
        final List<Link> links = Arrays.asList(fetchAllGuestsLink, searchGuestsLink, findGuestByNameLink);
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, links);
        return ResponseEntity.status(200).body(collectionModel);
    }

    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> findGuestByName(String firstName, String lastName) {
        final List<EntityModel<GuestDto>> guestEntityModels = service.findByName(new Name(firstName, lastName))
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link findGuestByNameLink = linkTo(methodOn(GuestControllerImpl.class).findGuestByName(firstName, lastName)).withSelfRel();
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, findGuestByNameLink);
        return ResponseEntity.status(200).body(collectionModel);
    }

    public ResponseEntity<CollectionModel<EntityModel<GuestDto>>> searchGuests(String search) {
        final List<EntityModel<GuestDto>> guestEntityModels = service.searchGuests(search)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        final Link searchGuestsLink = linkTo(methodOn(GuestControllerImpl.class).searchGuests(search)).withSelfRel();
        final CollectionModel<EntityModel<GuestDto>> collectionModel = CollectionModel.of(guestEntityModels, searchGuestsLink);
        return ResponseEntity.status(200).body(collectionModel);
    }

    @Override
    public ResponseEntity<EntityModel<GuestDto>> saveGuest(Guest guest) {
        guest.setStay(Stay.RESERVED);
        final GuestDto dto = service.saveGuest(guest);
        final EntityModel<GuestDto> entityModel = assembler.toModel(dto);
        final String path = "/" + Objects.requireNonNull(entityModel.getContent()).getId().toString();
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path(path).build().toUri();
        return ResponseEntity.status(201).location(uri).body(entityModel);
    }

    @Override
    public ResponseEntity<EntityModel<GuestDto>> updateGuest(Long id, Guest guest) {
        final String msg = "URL id and request body id do not match. URL path id: " + id + ". Request body id: " + guest.getId();
        if (!guest.getId().equals(id)) throw new UnmatchedIdException(msg);
        final Guest originalGuest = service.findGuestById(id);
        BeanUtils.copyProperties(guest, originalGuest, "id");
        final GuestDto updatedGuest = service.saveGuest(originalGuest);
        final EntityModel<GuestDto> entityModel = assembler.toModel(updatedGuest);
        return ResponseEntity.status(200).body(entityModel);
    }

    public ResponseEntity<EntityModel<Link>> deleteGuest(Long id) {
        service.deleteGuest(id);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> checkIn(Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.checkIn(guest));
        if (guestDto.getStay() == Stay.RESERVED) return ResponseEntity.status(200).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot check in guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).header(contentType, problemDetailsJsonValue).body(problem);
    }

    public ResponseEntity<?> checkOut(Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.checkOut(guest));
        if (guestDto.getStay() == Stay.CHECKED_IN) return ResponseEntity.status(200).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot check out guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).header(contentType, problemDetailsJsonValue).body(problem);
    }

    public ResponseEntity<?> cancelReserve(Long id) {
        final GuestDto guestDto = service.fetchGuest(id);
        final Guest guest = service.findGuestById(guestDto.getId());
        final EntityModel<GuestDto> entityModel = assembler.toModel(service.cancelReserve(guest));
        if (guestDto.getStay() == Stay.RESERVED) return ResponseEntity.status(200).body(entityModel);

        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final String detail = "Cannot cancel a reserve of a guest with stay status " + guestDto.getStay();
        final Problem problem = Problem.create().withTitle(METHOD_NOT_ALLOWED).withDetail(detail);
        return ResponseEntity.status(405).header(contentType, problemDetailsJsonValue).body(problem);
    }
}
