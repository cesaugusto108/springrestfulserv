package augusto108.ces.springrestfulserv.controllers.handlers;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Problem> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                e.getMessage() +
                        ". Acceptable formats: " + MediaType.APPLICATION_XML_VALUE +
                        " and " + MediaTypes.HAL_JSON_VALUE,
                e.toString());

        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create(response));
    }

    @ExceptionHandler({GuestNotFoundException.class, NoHandlerFoundException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleNotFound(Exception e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), e.toString());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create(response));
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleBadRequest(NumberFormatException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e.toString());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create(response));
    }

    private static class ErrorResponse {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        private final LocalDateTime timestamp;

        private final HttpStatus status;
        private final int statusCode;
        private final String message;
        private final String error;

        public ErrorResponse(HttpStatus status, String message, String error) {
            this.timestamp = LocalDateTime.now();
            this.status = status;
            this.statusCode = status.value();
            this.message = message;
            this.error = error;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public HttpStatus getStatus() {
            return status;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getMessage() {
            return message;
        }

        public String getError() {
            return error;
        }
    }
}
