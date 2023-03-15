package augusto108.ces.springrestfulserv.model;

public class Guest extends BaseEntity {
    private Name name;
    private Address address;
    private Telephone telephone;
    private String email;

    public Guest() {
    }

    public Guest(Name name, Address address, Telephone telephone, String email) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
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

    @Override
    public String toString() {
        return super.toString() + name +
                "[ " +
                address +
                ", " +
                telephone +
                ", " +
                email +
                "]";
    }
}
