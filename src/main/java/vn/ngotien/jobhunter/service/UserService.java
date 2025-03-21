package vn.ngotien.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.dto.Meta;
import vn.ngotien.jobhunter.domain.dto.ResultPaginationDTO;
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

  public ResultPaginationDTO getUserList(Pageable pageable) {
    Page<User> pageUser = this.userRepository.findAll(pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    Meta mt = new Meta();

    mt.setPage(pageUser.getNumber());
    mt.setPageSize(pageUser.getSize());

    mt.setPages(pageUser.getTotalPages());
    mt.setTotal(pageUser.getTotalElements());

    rs.setMeta(mt);
    rs.setResult(pageUser.getContent());

    return rs;
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
