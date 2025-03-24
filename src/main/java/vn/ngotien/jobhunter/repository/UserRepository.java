package vn.ngotien.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.ngotien.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  User save(User user);

  User findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsById(long id);

  User findByRefreshTokenAndEmail(String token, String email);

}
