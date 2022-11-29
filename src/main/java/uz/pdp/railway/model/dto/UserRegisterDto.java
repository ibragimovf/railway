package uz.pdp.railway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterDto {
    private String fullName;
    private String username;
    private String password;
    private String password2;
}
