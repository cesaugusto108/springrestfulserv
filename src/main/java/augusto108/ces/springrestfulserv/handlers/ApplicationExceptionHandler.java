package augusto108.ces.springrestfulserv.handlers;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    public ResponseEntity<Problem> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        final HttpStatus notAcceptable = HttpStatus.NOT_ACCEPTABLE;
        final String applicationXmlValue = MediaType.APPLICATION_XML_VALUE;
        final String applicationJsonValue = MediaType.APPLICATION_JSON_VALUE;
        final String message = e.getMessage() + ". Acceptable formats: " + applicationXmlValue + " and " + applicationJsonValue;
        final ErrorResponse response = new ErrorResponse(notAcceptable, message, e.toString());
        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final Problem problem = Problem.create(response);
        return ResponseEntity.status(406).header(contentType, problemDetailsJsonValue).body(problem);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({GuestNotFoundException.class, NoHandlerFoundException.class})
    @ResponseBody
    public ResponseEntity<Problem> handleNotFound(Exception e) {
        final HttpStatus notFound = HttpStatus.NOT_FOUND;
        final ErrorResponse response = new ErrorResponse(notFound, e.getMessage(), e.toString());
        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final Problem problem = Problem.create(response);
        return ResponseEntity.status(404).header(contentType, problemDetailsJsonValue).body(problem);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    @ResponseBody
    public ResponseEntity<Problem> handleBadRequest(NumberFormatException e) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorResponse response = new ErrorResponse(badRequest, e.getMessage(), e.toString());
        final String contentType = HttpHeaders.CONTENT_TYPE;
        final String problemDetailsJsonValue = MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE;
        final Problem problem = Problem.create(response);
        return ResponseEntity.status(400).header(contentType, problemDetailsJsonValue).body(problem);
    }

    public record ErrorResponse(
            LocalDateTime timestamp,
            HttpStatus status,
            int statusCode,
            String message,
            String error
    ) {

        public ErrorResponse(HttpStatus status, String message, String error) {
            this(LocalDateTime.now(), status, status.value(), message, error);
        }
    }
}
