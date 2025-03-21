package vn.ngotien.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ngotien.jobhunter.domain.dto.LoginDTO;
import vn.ngotien.jobhunter.domain.dto.ResLoginDTO;
import vn.ngotien.jobhunter.util.SecurityUtil;

@RestController
public class AuthController {

  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;

  public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.securityUtil = securityUtil;
  }

  // phase 1: data for "payload", this is from user data @authenticationToken ->
  // @authentication 1.1
  @PostMapping("/login")
  public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
    // Nạp input vào security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(), loginDTO.getPassword());
    // xác thực người dùng => cần viết hàm loadByUsername
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // create a token
    // phase 2 (next 2.4) : return token for login user 2.5
    String access_token = this.securityUtil.createToken(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ResLoginDTO res = new ResLoginDTO();
    res.setAccessToken(access_token);
    return ResponseEntity.ok().body(res);
  }

}
