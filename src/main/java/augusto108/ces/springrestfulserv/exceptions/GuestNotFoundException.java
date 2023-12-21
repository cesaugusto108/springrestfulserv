package augusto108.ces.springrestfulserv.exceptions;

public class GuestNotFoundException extends RuntimeException {

    public GuestNotFoundException(String message) {
        super(message);
    }
}
