package augusto108.ces.springrestfulserv.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
    @Column(name = "guest_address_street")
    private String street;

    @Column(name = "guest_address_number")
    private Integer number;

    @Column(name = "guest_address_city")
    private String city;

    public Address() {
    }

    public Address(String street, Integer number, String city) {
        this.street = street;
        this.number = number;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return street + ", " + number + " - " + city;
    }
}
