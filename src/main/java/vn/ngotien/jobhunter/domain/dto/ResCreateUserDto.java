package vn.ngotien.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.ngotien.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResCreateUserDto {
  private String email;
  private String name;
  private int age;
  private GenderEnum gender;
  private String address;
}
