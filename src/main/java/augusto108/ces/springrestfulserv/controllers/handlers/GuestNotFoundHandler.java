package augusto108.ces.springrestfulserv.controllers.handlers;

import org.hibernate.annotations.NotFound;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;

@ControllerAdvice
public class GuestNotFoundHandler {
    @ExceptionHandler
    @ResponseBody
    @NotFound
    public String handleException(GuestNotFoundException e) {
        return e.getMessage();
    }
}
