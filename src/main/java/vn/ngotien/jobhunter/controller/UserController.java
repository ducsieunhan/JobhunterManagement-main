package vn.ngotien.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.service.error.IdInvalidException;

@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createNewUser(@RequestBody User postmanUser) {
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
  public ResponseEntity<List<User>> fetchUserList() {
    List<User> usersList = this.userService.getUserList();
    return ResponseEntity.status(HttpStatus.OK).body(usersList);
  }

  @PutMapping("/users")
  public ResponseEntity<User> updateUser(@RequestBody User postmanUser) {
    User ducUser = this.userService.updateUser(postmanUser);
    return ResponseEntity.status(HttpStatus.OK).body(ducUser);
  }

}
