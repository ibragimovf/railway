package uz.pdp.railway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionDto {
    private String fullName;
    private String username;
    private String password;
    private String role;
    private List<String> permissions;
}
