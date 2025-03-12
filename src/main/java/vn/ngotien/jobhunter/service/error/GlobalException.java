package vn.ngotien.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.ngotien.jobhunter.domain.ResResponse;

/**
 * GlobalException class is a centralized exception handler for the application.
 * It provides a unified way to handle exceptions thrown by controllers and
 * return appropriate HTTP responses.
 * This class is annotated with {@link RestControllerAdvice} to indicate that it
 * contains global exception handling methods.
 * 
 * @author Your Name
 * @version 1.0
 * @since 2025-03-13
 */

@RestControllerAdvice
public class GlobalException {

  /**
   * Handles {@link IdInvalidException} and returns a {@link ResponseEntity} with
   * a 400 Bad Request status.
   * The response body contains the message from the exception.
   * 
   * @param idException the {@link IdInvalidException} that occurred
   * @return a {@link ResponseEntity} with a 400 Bad Request status and the
   *         exception message in the body
   */
  @ExceptionHandler(value = IdInvalidException.class)
  public ResponseEntity<ResResponse> handeIdException(IdInvalidException idException) {

    ResResponse<Object> res = new ResResponse<>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(idException.getMessage());
    res.setMessage("IdInvalidException");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

}
