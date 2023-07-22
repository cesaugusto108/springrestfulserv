package augusto108.ces.springrestfulserv.controllers.helpers;

import augusto108.ces.springrestfulserv.controllers.GuestController;
import augusto108.ces.springrestfulserv.model.Guest;
import augusto108.ces.springrestfulserv.model.enums.Stay;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuestModelAssembler implements RepresentationModelAssembler<Guest, EntityModel<Guest>> {
    @Override
    public EntityModel<Guest> toModel(Guest guest) {
        EntityModel<Guest> guestEntityModel = EntityModel.of(
                guest,
                linkTo(methodOn(GuestController.class).fetchGuest(guest.getId())).withSelfRel(),
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withRel("guests")
        );

        confirmReserveStatus(guest, guestEntityModel);
        confirmCheckInStatus(guest, guestEntityModel);

        return guestEntityModel;
    }

    private static void confirmReserveStatus(Guest guest, EntityModel<Guest> guestEntityModel) {
        if (guest.getStay() == Stay.RESERVED) {
            guestEntityModel
                    .add(linkTo(methodOn(GuestController.class).checkIn(guest.getId()))
                            .withRel("check-in"));
            guestEntityModel
                    .add(linkTo(methodOn(GuestController.class).cancelReserve(guest.getId()))
                            .withRel("cancel-reserve"));

        }
    }

    private static void confirmCheckInStatus(Guest guest, EntityModel<Guest> guestEntityModel) {
        if (guest.getStay() == Stay.CHECKED_IN) {
            guestEntityModel
                    .add(linkTo(methodOn(GuestController.class).checkOut(guest.getId()))
                            .withRel("check-out"));
        }
    }
}
