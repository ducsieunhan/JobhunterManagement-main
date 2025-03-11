package vn.ngotien.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.repository.UserRepository;

@Service
public class UserService {
  UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void createNewUser(User user) {
    this.userRepository.save(user);
  }

}
