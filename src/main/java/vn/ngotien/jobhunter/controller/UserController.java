package vn.ngotien.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.service.UserService;
import vn.ngotien.jobhunter.service.error.IdInvalidException;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if(id > 30){
            throw new IdInvalidException("Id not found");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User optionalUser = this.userService.handleFindUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(optionalUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.findAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        this.userService.hanldleUpdateUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
