package vn.ngotien.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.LoginDTO;
import vn.ngotien.jobhunter.domain.dto.ResLoginDTO;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

  private final UserService userService;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final SecurityUtil securityUtil;

  @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenExpiration;

  public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
      UserService userService) {
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.securityUtil = securityUtil;
    this.userService = userService;
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
    String access_token = this.securityUtil.createAccessToken(authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ResLoginDTO res = new ResLoginDTO();

    User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
    if (currentUserDB != null) {
      ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
          currentUserDB.getName());
      res.setUser(userLogin);
    }

    res.setAccessToken(access_token);

    // create refresh token
    String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

    // update user
    this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

    // set cookies
    ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookie.toString())
        .body(res);
  }

}
