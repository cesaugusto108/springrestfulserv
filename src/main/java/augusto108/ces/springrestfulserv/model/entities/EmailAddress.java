package augusto108.ces.springrestfulserv.model.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public final class EmailAddress {
    @Column(name = "guest_email_username")
    private String username;

    @Column(name = "guest_email_domain_name")
    private String domainName;

    public EmailAddress() {
    }

    public EmailAddress(String username, String domainName) {
        this.username = username;

        if (!domainName.startsWith("@")) {
            this.domainName = "@" + domainName;

            return;
        }

        this.domainName = domainName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        if (!domainName.startsWith("@")) {
            this.domainName = "@" + domainName;

            return;
        }

        this.domainName = domainName;
    }

    @Override
    public String toString() {
        return username + domainName;
    }
}
