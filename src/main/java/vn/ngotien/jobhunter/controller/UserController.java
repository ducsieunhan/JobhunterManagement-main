package vn.ngotien.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.ResultPaginationDTO;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.util.annotation.ApiMessage;
import vn.ngotien.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
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
  @ApiMessage("Fetch all users")
  public ResponseEntity<ResultPaginationDTO> fetchUserList(
      @Filter Specification<User> spec,
      Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserList(spec, pageable));
  }

  @PutMapping("/users")
  public ResponseEntity<User> updateUser(@RequestBody User postmanUser) {
    User ducUser = this.userService.updateUser(postmanUser);
    return ResponseEntity.status(HttpStatus.OK).body(ducUser);
  }

}
