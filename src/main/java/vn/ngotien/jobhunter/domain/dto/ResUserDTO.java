package vn.ngotien.jobhunter.domain.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.ngotien.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResUserDTO {
  private String email;
  private String name;
  private int age;
  private GenderEnum gender;
  private String address;
  private Instant createdAt;
  private String createdBy;
  private Instant updatedAt;
  private String updatedBy;
}
