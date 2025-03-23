package vn.ngotien.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.ngotien.jobhunter.util.SecurityUtil;
import vn.ngotien.jobhunter.util.constant.GenderEnum;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String name;
  private String password;

  private int age;

  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  private String address;
  private String refreshToken;
  private Instant createdAt;
  private String createdBy;
  private Instant updatedAt;
  private String updatedBy;

  @PrePersist
  public void handleBeforeCreate() {
    this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? this.createdBy = SecurityUtil.getCurrentUserLogin().get()
        : "";
    this.createdAt = Instant.now();
  }

  @PreUpdate
  public void handleBeforeUpdate() {
    this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
        ? this.updatedBy = SecurityUtil.getCurrentUserLogin().get()
        : "";
    this.updatedAt = Instant.now();
  }
}
