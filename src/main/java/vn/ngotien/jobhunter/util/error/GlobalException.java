package vn.ngotien.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.ngotien.jobhunter.domain.response.ResResponse;

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
  @ExceptionHandler(value = {
      UsernameNotFoundException.class,
      BadCredentialsException.class,
  })
  public ResponseEntity<ResResponse<Object>> handleIdException(Exception ex) {

    ResResponse<Object> res = new ResResponse<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getMessage());
    res.setMessage("Exception occurs... ");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(value = {
      NoResourceFoundException.class,
  })
  public ResponseEntity<ResResponse<Object>> handleNotFoundResource(Exception ex) {

    ResResponse<Object> res = new ResResponse<Object>();
    res.setStatusCode(HttpStatus.NOT_FOUND.value());
    res.setError(ex.getMessage());
    res.setMessage("404 not found exception ");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  /**
   * Xử lý ngoại lệ {@link MethodArgumentNotValidException} khi dữ liệu đầu vào
   * không hợp lệ.
   * Phương thức này thu thập các thông báo lỗi từ các trường (field) không hợp lệ
   * và trả về một phản hồi thống nhất cho client.
   *
   * @param ex Ngoại lệ {@link MethodArgumentNotValidException} chứa thông tin về
   *           các lỗi validation.
   * @return {@link ResponseEntity} chứa thông tin lỗi và mã trạng thái HTTP 400
   *         (Bad Request).
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResResponse<Object>> validationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    final List<FieldError> fieldErrors = result.getFieldErrors();

    ResResponse<Object> res = new ResResponse<Object>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError(ex.getBody().getDetail());

    List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
    res.setMessage(errors.size() > 1 ? errors : errors.get(0));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

}
