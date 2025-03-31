package vn.ngotien.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.ResLoginDTO;
import vn.ngotien.jobhunter.domain.request.ReqLoginDTO;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.util.SecurityUtil;
import vn.ngotien.jobhunter.util.annotation.ApiMessage;
import vn.ngotien.jobhunter.util.error.IdInvalidException;

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
  @PostMapping("/auth/login")
  public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDTO) {
    // Nạp input vào security
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        loginDTO.getUsername(), loginDTO.getPassword());
    // xác thực người dùng => cần viết hàm loadByUsername
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // create a token
    // phase 2 (next 2.4) : return token for login user 2.5
    SecurityContextHolder.getContext().setAuthentication(authentication);

    ResLoginDTO res = new ResLoginDTO();

    User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
    if (currentUserDB != null) {
      ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
          currentUserDB.getName());
      res.setUser(userLogin);
    }

    String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());

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

  @GetMapping("/auth/account")
  @ApiMessage("Fetch account")
  public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
    String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

    User currentUserDB = this.userService.handleGetUserByUsername(email);
    ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
    ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

    if (currentUserDB != null) {
      userLogin.setEmail(currentUserDB.getEmail());
      userLogin.setId(currentUserDB.getId());
      userLogin.setName(currentUserDB.getName());
      userGetAccount.setUser(userLogin);
    }
    return ResponseEntity.ok().body(userGetAccount);
  }

  @GetMapping("/auth/refresh")
  @ApiMessage("Get user by refresh token")
  public ResponseEntity<ResLoginDTO> getRefreshToken(
      @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) throws IdInvalidException {

    if (refresh_token.equals("abc")) {
      throw new IdInvalidException("You don't have access token ");
    }
    // check valid token
    Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
    String email = decodedToken.getSubject();

    // check user by token email
    User currentUser = this.userService.getUserByRefreshTokenAndEmail(refresh_token, email);
    if (currentUser == null) {
      throw new IdInvalidException("Refresh Token invalid");
    }

    ResLoginDTO res = new ResLoginDTO();

    User currentUserDB = this.userService.handleGetUserByUsername(email);
    if (currentUserDB != null) {
      ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
          currentUserDB.getName());
      res.setUser(userLogin);
    }

    String access_token = this.securityUtil.createAccessToken(email, res.getUser());

    res.setAccessToken(access_token);

    // create refresh token
    String new_refresh_token = this.securityUtil.createRefreshToken(email, res);

    // update user
    this.userService.updateUserToken(refresh_token, email);

    // set cookies
    ResponseCookie resCookie = ResponseCookie.from("refresh_token", new_refresh_token)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(refreshTokenExpiration)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookie.toString())
        .body(res);
  }

  @GetMapping("/auth/logout")
  @ApiMessage("Logout user")
  public ResponseEntity<Void> logout() throws IdInvalidException {
    String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

    if (email.equals("")) {
      throw new IdInvalidException("Access token invalid");
    }

    this.userService.updateUserToken(null, email);

    ResponseCookie resCookie = ResponseCookie.from("refresh_token", null)
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, resCookie.toString())
        .body(null);
  }

}
