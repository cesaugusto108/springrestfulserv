package augusto108.ces.springrestfulserv.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class Telephone {
    @Column(name = "guest_telephone")
    private String telephone;

    public Telephone() {
    }

    public Telephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return telephone;
    }
}
