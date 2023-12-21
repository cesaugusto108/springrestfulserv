package augusto108.ces.springrestfulserv.assemblers;

import augusto108.ces.springrestfulserv.controllers.GuestControllerImpl;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuestModelAssembler implements RepresentationModelAssembler<GuestDto, EntityModel<GuestDto>> {

    @Override
    public @NotNull EntityModel<GuestDto> toModel(GuestDto guestDto) {
        final Link fetchGuestLink = linkTo(methodOn(GuestControllerImpl.class).fetchGuest(guestDto.getId())).withSelfRel();
        final Link fetchAllGuestsLink = linkTo(methodOn(GuestControllerImpl.class).fetchAllGuests()).withRel("guests");
        EntityModel<GuestDto> guestEntityModel = EntityModel.of(guestDto, fetchGuestLink, fetchAllGuestsLink);
        confirmReserveStatus(guestDto, guestEntityModel);
        confirmCheckInStatus(guestDto, guestEntityModel);
        return guestEntityModel;
    }

    private static void confirmReserveStatus(GuestDto guestDto, EntityModel<GuestDto> guestDtoEntityModel) {
        final Link checkInLink = linkTo(methodOn(GuestControllerImpl.class).checkIn(guestDto.getId())).withRel("check-in");
        final Link cancelReserveLink = linkTo(methodOn(GuestControllerImpl.class).cancelReserve(guestDto.getId())).withRel("cancel-reserve");
        if (guestDto.getStay() == Stay.RESERVED) guestDtoEntityModel.add(checkInLink, cancelReserveLink);
    }

    private static void confirmCheckInStatus(GuestDto guestDto, EntityModel<GuestDto> guestDtoEntityModel) {
        final Link checkOutLink = linkTo(methodOn(GuestControllerImpl.class).checkOut(guestDto.getId())).withRel("check-out");
        if (guestDto.getStay() == Stay.CHECKED_IN) guestDtoEntityModel.add(checkOutLink);
    }
}
