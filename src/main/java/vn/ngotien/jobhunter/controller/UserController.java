package vn.ngotien.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.service.UserService;

@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/user/create")
  public String createNewUser() {

    User user = new User();
    user.setEmail("chroller01@gmail.com");
    user.setName("ducngominh");
    user.setPassword("0209Ng");
    this.userService.createNewUser(user);

    return "create user";
  }
}
