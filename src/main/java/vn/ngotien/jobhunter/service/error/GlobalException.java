package vn.ngotien.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vn.ngotien.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object>> idInvalidException(IdInvalidException e) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(e.getMessage());
        restResponse.setMessage("IdInvalidException");
        return ResponseEntity.badRequest().body(restResponse);
    }
}
