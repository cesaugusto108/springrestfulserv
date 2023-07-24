package augusto108.ces.springrestfulserv.mapper;

import augusto108.ces.springrestfulserv.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.entities.Guest;

import java.util.ArrayList;
import java.util.List;

public class DtoMapper {
    public static GuestDto fromGuestToGuestDto(Guest guest) {
        final GuestDto guestDto = new GuestDto();

        guestDto.setName(guest.getName());
        guestDto.setAddress(guest.getAddress());
        guestDto.setTelephone(guest.getTelephone());
        guestDto.setEmailAddress(guest.getEmailAddress());
        guestDto.setStay(guest.getStay());
        guestDto.setId(guest.getId());

        return guestDto;
    }

    public static List<GuestDto> fromGuestListToGuestDtoList(List<Guest> guests) {
        final List<GuestDto> guestDtoList = new ArrayList<>();

        for (Guest guest : guests) {
            guestDtoList.add(fromGuestToGuestDto(guest));
        }

        return guestDtoList;
    }
}
