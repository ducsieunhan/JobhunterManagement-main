package vn.ngotien.jobhunter.service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

  public ResponseEntity<String> handeIdException(IdInvalidException idException) {
    return ResponseEntity.badRequest().body(idException.getMessage());
  }

}
