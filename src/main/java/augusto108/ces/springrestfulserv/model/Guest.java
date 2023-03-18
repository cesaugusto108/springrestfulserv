package augusto108.ces.springrestfulserv.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import augusto108.ces.springrestfulserv.model.enums.Stay;

@Entity
@Table(name = "tb_guest")
public class Guest extends BaseEntity {
    @Embedded
    private Name name;

    @Embedded
    private Address address;

    @Embedded
    private Telephone telephone;

    @Column(name = "guest_email")
    private String email;

    @Enumerated
    private Stay stay;

    public Guest() {
    }

    public Guest(Name name, Address address, Telephone telephone, String email, Stay stay) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
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
                "] Stay status: " +
                stay;
    }
}
