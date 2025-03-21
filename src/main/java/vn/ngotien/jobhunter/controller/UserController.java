package vn.ngotien.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.ResultPaginationDTO;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.util.error.IdInvalidException;

@RestController
public class UserController {

  private UserService userService;
  private PasswordEncoder passwordEncoder;

  public UserController(UserService userService, PasswordEncoder passwordEncoder) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createNewUser(@RequestBody User postmanUser) {
    String hashPassword = passwordEncoder.encode(postmanUser.getPassword());
    postmanUser.setPassword(hashPassword);
    User ducUser = this.userService.createNewUser(postmanUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(ducUser);
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
    if (id > 1500) {
      throw new IdInvalidException("Can not match with any id ");
    }
    this.userService.deleteUserById(id);

    return ResponseEntity.ok("new user");
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> fetchUserById(@PathVariable("id") long id) {
    User currentUser = this.userService.getUserById(id);

    return ResponseEntity.status(HttpStatus.OK).body(currentUser);
  }

  @GetMapping("/users")
  public ResponseEntity<ResultPaginationDTO> fetchUserList(
      @RequestParam("current") Optional<String> currentOptional,
      @RequestParam("pageSize") Optional<String> pageSizeOptional) {
    String sCurrent = currentOptional.isPresent() ? currentOptional.get().trim() : "";
    String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get().trim() : "";

    Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));

    ResultPaginationDTO rs = this.userService.getUserList(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(rs);
  }

  @PutMapping("/users")
  public ResponseEntity<User> updateUser(@RequestBody User postmanUser) {
    User ducUser = this.userService.updateUser(postmanUser);
    return ResponseEntity.status(HttpStatus.OK).body(ducUser);
  }

}
