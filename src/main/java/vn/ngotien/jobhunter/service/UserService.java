package vn.ngotien.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import vn.ngotien.jobhunter.controller.AuthController;
import vn.ngotien.jobhunter.controller.UserController;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.repository.UserRepository;

@Service
public class UserService {

  UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createNewUser(User user) {
    return this.userRepository.save(user);
  }

  public void deleteUserById(long id) {
    this.userRepository.deleteById(id);
  }

  public User getUserById(long id) {
    Optional<User> userOptional = this.userRepository.findById(id);
    if (userOptional.isPresent()) {
      return userOptional.get();
    }
    return null;
  }

  public List<User> getUserList() {
    return this.userRepository.findAll();
  }

  public User updateUser(User user) {
    User currentUser = this.getUserById(user.getId());
    if (currentUser != null) {
      currentUser.setEmail(user.getEmail());
      currentUser.setName(user.getName());
      currentUser.setPassword(user.getPassword());
    }
    return this.userRepository.save(currentUser);
  }

  public User handleGetUserByUsername(String username) {
    return this.userRepository.findByEmail(username);
  }

}
