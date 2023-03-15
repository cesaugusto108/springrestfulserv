package augusto108.ces.springrestfulserv.controllers.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;

@ControllerAdvice
public class GuestNotFoundHandler {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleException(GuestNotFoundException e) {
        return e.getMessage();
    }
}
