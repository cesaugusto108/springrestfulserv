package augusto108.ces.springrestfulserv.controllers.handlers;

import augusto108.ces.springrestfulserv.exceptions.GuestNotFoundException;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class GuestNotFoundHandler {
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Problem> handleException(GuestNotFoundException e) {
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), e.toString());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
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
