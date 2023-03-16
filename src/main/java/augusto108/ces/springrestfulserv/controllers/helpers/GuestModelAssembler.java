package augusto108.ces.springrestfulserv.controllers.helpers;

import augusto108.ces.springrestfulserv.controllers.GuestController;
import augusto108.ces.springrestfulserv.model.Guest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GuestModelAssembler
        implements RepresentationModelAssembler<Guest, EntityModel<Guest>> {
    @Override
    public EntityModel<Guest> toModel(Guest guest) {
        return EntityModel.of(
                guest,
                linkTo(methodOn(GuestController.class).fetchGuest(guest.getId())).withSelfRel(),
                linkTo(methodOn(GuestController.class).fetchAllGuests()).withRel("guests")
        );
    }

    @Override
    public CollectionModel<EntityModel<Guest>> toCollectionModel(Iterable<? extends Guest> guests) {
        return RepresentationModelAssembler.super.toCollectionModel(guests);
    }
}
