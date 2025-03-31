package vn.ngotien.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.domain.response.ResCreateUserDto;
import vn.ngotien.jobhunter.domain.response.ResUpdateUserDto;
import vn.ngotien.jobhunter.domain.response.ResUserDTO;
import vn.ngotien.jobhunter.domain.response.ResultPaginationDTO;
import vn.ngotien.jobhunter.repository.UserRepository;

@Service
public class UserService {

  UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public boolean checkExistEmail(String email) {
    return this.userRepository.existsByEmail(email);
  }

  public boolean checkExistById(long id) {
    return this.userRepository.existsById(id);
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

  public ResultPaginationDTO getUserList(Specification<User> spec, Pageable pageable) {
    Page<User> pageUser = this.userRepository.findAll(spec, pageable);
    ResultPaginationDTO rs = new ResultPaginationDTO();
    ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

    mt.setPage(pageUser.getNumber() + 1);
    mt.setPageSize(pageUser.getSize());

    mt.setPages(pageUser.getTotalPages());
    mt.setTotal(pageUser.getTotalElements());

    rs.setMeta(mt);
    List<ResUserDTO> userDTOs = new ArrayList<>();

    for (User user : pageUser) {
      ResUserDTO newUser = this.userToDTO(user);
      userDTOs.add(newUser);
    }
    rs.setResult(userDTOs);

    return rs;
  }

  public User updateUser(User user) {
    User currentUser = this.getUserById(user.getId());
    if (currentUser != null) {
      currentUser.setName(user.getName());
      currentUser.setPassword(user.getPassword());
      currentUser.setAddress(user.getAddress());
      currentUser.setGender(user.getGender());
      currentUser.setAge(user.getAge());
    }
    return this.userRepository.save(currentUser);
  }

  public User handleGetUserByUsername(String username) {
    return this.userRepository.findByEmail(username);
  }

  public ResCreateUserDto createUserDTO(User user) {
    User newUser = this.createNewUser(user);
    return new ResCreateUserDto(newUser.getEmail(), newUser.getName(), newUser.getAge(), newUser.getGender(),
        newUser.getAddress());
  }

  public ResUpdateUserDto updateUserDTO(User user) {
    User newUser = this.updateUser(user);
    return new ResUpdateUserDto(newUser.getId(), newUser.getName(), newUser.getAge(), newUser.getGender(),
        newUser.getAddress(), newUser.getUpdatedAt());
  }

  public ResUserDTO getAUserDTO(long id) {
    User user = this.getUserById(id);
    return userToDTO(user);
  }

  public ResUserDTO userToDTO(User user) {
    ResUserDTO newUserDTO = new ResUserDTO();
    newUserDTO.setEmail(user.getEmail());
    newUserDTO.setName(user.getName());
    newUserDTO.setAddress(user.getAddress());
    newUserDTO.setAge(user.getAge());
    newUserDTO.setGender(user.getGender());
    newUserDTO.setCreatedAt(user.getCreatedAt());
    newUserDTO.setCreatedBy(user.getCreatedBy());
    newUserDTO.setUpdatedAt(user.getUpdatedAt());
    newUserDTO.setUpdatedBy(user.getUpdatedBy());
    return newUserDTO;
  }

  public void updateUserToken(String token, String email) {
    User currentUser = this.handleGetUserByUsername(email);

    if (currentUser != null) {
      currentUser.setRefreshToken(token);
      this.userRepository.save(currentUser);
    }
  }

  public User getUserByRefreshTokenAndEmail(String token, String email) {
    return this.userRepository.findByRefreshTokenAndEmail(token, email);
  }
}
