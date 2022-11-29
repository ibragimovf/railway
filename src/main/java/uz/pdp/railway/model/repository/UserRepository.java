package uz.pdp.railway.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.railway.model.entity.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
