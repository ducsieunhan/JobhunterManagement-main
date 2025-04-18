package vn.ngotien.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.ngotien.jobhunter.domain.dto.ResResponse;
import vn.ngotien.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

  // for every response
  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

    HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
    int status = servletResponse.getStatus();

    ResResponse<Object> res = new ResResponse<>();
    res.setStatusCode(status);

    if (body instanceof String) {
      return body;
    }

    if (status >= 400) {
      return body;
    } else {
      res.setData(body);
      ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
      res.setMessage(message != null ? message.value() : "Call api success");
    }

    return res;
  }

}
