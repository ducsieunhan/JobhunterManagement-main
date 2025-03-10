package vn.ngotien.jobhunter.service;

import org.springframework.stereotype.Service;
import vn.ngotien.jobhunter.domain.User;
import vn.ngotien.jobhunter.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
         return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User handleFindUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElse(null);
    }

    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    public void hanldleUpdateUser(User user) {
        //Lay id user can update
        long id = user.getId();
        //Lay ra user tu db
        Optional<User> userOptional = this.userRepository.findById(id);
        //Sua thong tin
        if (userOptional.isPresent()) {
            userOptional.get().setName(user.getName());
            userOptional.get().setEmail(user.getEmail());
            userOptional.get().setPassword(user.getPassword());
            this.userRepository.save(userOptional.get());
        }
    }
}
