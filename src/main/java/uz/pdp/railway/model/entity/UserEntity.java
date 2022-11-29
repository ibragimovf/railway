package uz.pdp.railway.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fullName;
    private String username;
    private String password;
    private boolean isActive;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    private List<RoleEntity> roleEntityList;

    private String permissions;

    public UserEntity(String fullName, String username, String password, List<RoleEntity> roleEntityList, boolean isActive) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.roleEntityList = roleEntityList;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();

        roleEntityList.forEach((roleEntity -> {
            SimpleGrantedAuthority role =
                    new SimpleGrantedAuthority("ROLE_" + roleEntity.getAuthority());
            roles.add(role);
        }));

        try {
            if (permissions != null) {
                String[] permissions = new ObjectMapper().readValue(this.permissions, String[].class);
                for (String s : permissions) {
                    roles.add(new SimpleGrantedAuthority(s));
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
