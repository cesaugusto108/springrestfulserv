package augusto108.ces.springrestfulserv.model.mapper;

import augusto108.ces.springrestfulserv.model.dto.v1.GuestDto;
import augusto108.ces.springrestfulserv.model.entities.Guest;

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
        guests.forEach(guest -> guestDtoList.add(fromGuestToGuestDto(guest)));
        return guestDtoList;
    }
}
