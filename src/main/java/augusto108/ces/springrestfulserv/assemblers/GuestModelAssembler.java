package augusto108.ces.springrestfulserv.assemblers;

import augusto108.ces.springrestfulserv.controllers.GuestController;
import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuestModelAssembler implements RepresentationModelAssembler<GuestDto, EntityModel<GuestDto>> {
    @Override
    public EntityModel<GuestDto> toModel(GuestDto guestDto) {
        EntityModel<GuestDto> guestEntityModel = EntityModel.of(
                guestDto,
                linkTo(methodOn(GuestController.class).fetchGuest(guestDto.getId())).withSelfRel(),
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withRel("guests")
        );

        confirmReserveStatus(guestDto, guestEntityModel);
        confirmCheckInStatus(guestDto, guestEntityModel);

        return guestEntityModel;
    }

    private static void confirmReserveStatus(GuestDto guestDto, EntityModel<GuestDto> guestDtoEntityModel) {
        if (guestDto.getStay() == Stay.RESERVED) {
            guestDtoEntityModel
                    .add(linkTo(methodOn(GuestController.class).checkIn(guestDto.getId()))
                            .withRel("check-in"));
            guestDtoEntityModel
                    .add(linkTo(methodOn(GuestController.class).cancelReserve(guestDto.getId()))
                            .withRel("cancel-reserve"));

        }
    }

    private static void confirmCheckInStatus(GuestDto guestDto, EntityModel<GuestDto> guestDtoEntityModel) {
        if (guestDto.getStay() == Stay.CHECKED_IN) {
            guestDtoEntityModel
                    .add(linkTo(methodOn(GuestController.class).checkOut(guestDto.getId()))
                            .withRel("check-out"));
        }
    }
}
