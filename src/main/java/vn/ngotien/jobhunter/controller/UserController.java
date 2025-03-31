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
import vn.ngotien.jobhunter.domain.dto.ResCreateUserDto;
import vn.ngotien.jobhunter.domain.dto.ResUpdateUserDto;
import vn.ngotien.jobhunter.domain.dto.ResultPaginationDTO;
import vn.ngotien.jobhunter.domain.dto.ResUserDTO;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.util.annotation.ApiMessage;
import vn.ngotien.jobhunter.util.error.EmailInvalidException;
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
  @ApiMessage("Create a new user")
  public ResponseEntity<ResCreateUserDto> createNewUser(@RequestBody User postmanUser) throws EmailInvalidException {
    if (this.userService.checkExistEmail(postmanUser.getEmail())) {
      throw new EmailInvalidException("This email already exists!");
    }
    String hashPassword = passwordEncoder.encode(postmanUser.getPassword());
    postmanUser.setPassword(hashPassword);
    ResCreateUserDto ducUser = this.userService.createUserDTO(postmanUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(ducUser);
  }

  @DeleteMapping("/users/{id}")
  @ApiMessage("Delete user")
  public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
    if (!this.userService.checkExistById(id)) {
      throw new IdInvalidException("This account is not exist !!");
    }
    this.userService.deleteUserById(id);
    return ResponseEntity.ok("Successfully deleted user ");
  }

  @GetMapping("/users/{id}")
  @ApiMessage("Get a user")
  public ResponseEntity<ResUserDTO> fetchUserById(@PathVariable("id") long id) throws IdInvalidException {
    if (!this.userService.checkExistById(id)) {
      throw new IdInvalidException("This account is not exist !!");
    }
    return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAUserDTO(id));
  }

  @GetMapping("/users")
  @ApiMessage("Fetch all users")
  public ResponseEntity<ResultPaginationDTO> fetchUserList(
      @Filter Specification<User> spec,
      Pageable pageable) {

    return ResponseEntity.status(HttpStatus.OK).body(this.userService.getUserList(spec, pageable));
  }

  @PutMapping("/users")
  @ApiMessage("Update a user")
  public ResponseEntity<ResUpdateUserDto> updateUser(@RequestBody User postmanUser) throws IdInvalidException {
    if (!this.userService.checkExistById(postmanUser.getId())) {
      throw new IdInvalidException("This account is not exist !!");
    }
    ResUpdateUserDto ducUser = this.userService.updateUserDTO(postmanUser);
    return ResponseEntity.status(HttpStatus.OK).body(ducUser);
  }

}
