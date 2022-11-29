package uz.pdp.railway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.railway.model.dto.PermissionDto;
import uz.pdp.railway.model.dto.UserLoginDto;
import uz.pdp.railway.model.dto.UserRegisterDto;
import uz.pdp.railway.model.entity.RoleEntity;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.model.enums.RoleEnum;
import uz.pdp.railway.model.repository.RoleRepository;
import uz.pdp.railway.model.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;


    public UserService(PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public List<UserEntity> getUsers(Integer pageSize) {
        Pageable page = PageRequest.of(pageSize - 1, 6, Sort.by("id").descending());
        return userRepository.findAll(page).stream().toList();
    }

    public void addSuperAdmin() {
        roleRepository.save(new RoleEntity(RoleEnum.SUPER_ADMIN.name()));
        roleRepository.save(new RoleEntity(RoleEnum.ADMIN.name()));
        roleRepository.save(new RoleEntity(RoleEnum.USER.name()));
        UserEntity user = new UserEntity();
        user.setFullName("Super admin");
        user.setUsername("root");
        user.setPassword(passwordEncoder.encode("root"));
        user.setActive(true);
        user.setRoleEntityList(List.of(roleRepository.findByAuthority(RoleEnum.SUPER_ADMIN.name()).get()));
        userRepository.save(user);
    }

    public boolean addPermission(final PermissionDto permissionDto) throws JsonProcessingException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(permissionDto.getUsername());

        if (optionalUserEntity.isPresent()) return false;

        UserEntity user = new UserEntity();
        user.setFullName(permissionDto.getFullName());
        user.setUsername(permissionDto.getUsername());
        user.setPassword(passwordEncoder.encode(permissionDto.getPassword()));
        user.setRoleEntityList(roleRepository.findByAuthority(permissionDto.getRole()).stream().toList());
        user.setPermissions(objectMapper.writeValueAsString(permissionDto.getPermissions()));
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    public boolean login(Optional<UserLoginDto> userLogin) {
        if (userLogin.isPresent()) {
            if (userRepository.findByUsername(userLogin.get().getUsername()).isPresent()) {
                UserEntity user = userRepository.findByUsername(userLogin.get().getUsername()).get();
                return passwordEncoder.matches(userLogin.get().getPassword(), user.getPassword());
            }
        }
        return false;
    }

    public boolean wrongPass(Optional<UserRegisterDto> userRegister) {
        if (userRegister.isPresent()){
            UserRegisterDto registerDto = userRegister.get();
            return !registerDto.getPassword().equals(registerDto.getPassword2());
        }
        return true;
    }

    public boolean register(Optional<UserRegisterDto> userRegister) {

        if (userRepository.findByUsername(userRegister.get().getUsername()).isPresent()) {
            return false;
        }

        if (wrongPass(userRegister)) {
            return false;
        } else {
            UserEntity user = new UserEntity();
            user.setFullName(userRegister.get().getFullName());
            user.setUsername(userRegister.get().getUsername());
            user.setPassword(passwordEncoder.encode(userRegister.get().getPassword()));
            user.setActive(true);
            user.setRoleEntityList(List.of(roleRepository.findByAuthority(RoleEnum.USER.name()).get()));
            userRepository.save(user);
            return true;
        }
    }

}
