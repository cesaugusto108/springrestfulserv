package augusto108.ces.springrestfulserv.model.entities;

import augusto108.ces.springrestfulserv.model.enums.Stay;

import javax.persistence.*;

@Entity
@Table(name = "tb_guest")
public final class Guest extends BaseEntity {
    @Embedded
    private Name name;

    @Embedded
    private Address address;

    @Embedded
    private Telephone telephone;

    @Column(name = "guest_email")
    private String email;

    @Embedded
    private EmailAddress emailAddress;

    @Enumerated(EnumType.STRING)
    private Stay stay;

    public Guest() {
    }

    public Guest(Name name, Address address, Telephone telephone, String email, EmailAddress emailAddress, Stay stay) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        return super.toString() +
                " - " +
                name +
                " [" +
                address +
                ", " +
                telephone +
                ", " +
                email +
                " (" +
                emailAddress +
                ")] Stay status: " +
                stay;
    }
}
