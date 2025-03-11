package vn.ngotien.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.service.UserService;

@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user/create")
  public User createNewUser(@RequestBody User postmanUser) {
    User ducUser = this.userService.createNewUser(postmanUser);
    return ducUser;
  }

  // @DeleteMapping("/user/{id}")
  // public void deleteUser(@PathVariable("id") long id) {
  // this.userService.deleteUserById(id);
  // }

  @GetMapping("/user/{id}")
  public User fetchUserById(@PathVariable("id") long id) {
    return this.userService.getUserById(id);

  }

  @GetMapping("/user")
  public List<User> fetchUserList() {
    return this.userService.getUserList();
  }

  @PutMapping("/user")
  public User updateUser(@RequestBody User postmanUser) {
    User ducUser = this.userService.updateUser(postmanUser);
    return ducUser;
  }

}
