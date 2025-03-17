package vn.ngotien.jobhunter.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import vn.ngotien.jobhunter.controller.AuthController;

// ke thua cho authentication 
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {

  private final AuthController authController;

  private final UserService userService;

  public UserDetailsCustom(UserService userService, AuthController authController) {
    this.userService = userService;
    this.authController = authController;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    vn.ngotien.jobhunter.domain.User user = this.userService.handleGetUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Username/Password invalid ");
    }

    // TODO Auto-generated method stub
    return new User(
        user.getEmail(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
  }

}
