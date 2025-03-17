package vn.ngotien.jobhunter.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
  @NotBlank(message = "Username can not be empty")
  private String username;

  @NotBlank(message = "Password can not be empty")
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}