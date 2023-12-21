package augusto108.ces.springrestfulserv.model.dto.v1;

import augusto108.ces.springrestfulserv.model.datatypes.Address;
import augusto108.ces.springrestfulserv.model.datatypes.EmailAddress;
import augusto108.ces.springrestfulserv.model.datatypes.Name;
import augusto108.ces.springrestfulserv.model.datatypes.Telephone;
import augusto108.ces.springrestfulserv.model.enums.Stay;

public final class GuestDto extends BaseDto {

    private Name name;
    private Address address;
    private Telephone telephone;
    private EmailAddress emailAddress;
    private Stay stay;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Telephone getTelephone() {
        return telephone;
    }

    public void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Stay getStay() {
        return stay;
    }

    public void setStay(Stay stay) {
        this.stay = stay;
    }
}
