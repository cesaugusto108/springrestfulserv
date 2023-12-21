package augusto108.ces.springrestfulserv.model.dto.v1;

import augusto108.ces.springrestfulserv.model.entities.Address;
import augusto108.ces.springrestfulserv.model.entities.EmailAddress;
import augusto108.ces.springrestfulserv.model.entities.Name;
import augusto108.ces.springrestfulserv.model.entities.Telephone;
import augusto108.ces.springrestfulserv.model.enums.Stay;

public final class GuestDto extends BaseDto {
    private Name name;
    private Address address;
    private Telephone telephone;
    private EmailAddress emailAddress;
    private Stay stay;

    public GuestDto() {
    }

    public GuestDto(Name name, Address address, Telephone telephone, EmailAddress emailAddress, Stay stay) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.emailAddress = emailAddress;
        this.stay = stay;
    }

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

    @Override
    public String toString() {
        return "GuestDto{" +
                "name=" + name +
                ", address=" + address +
                ", telephone=" + telephone +
                ", emailAddress=" + emailAddress +
                ", stay=" + stay +
                '}';
    }
}
