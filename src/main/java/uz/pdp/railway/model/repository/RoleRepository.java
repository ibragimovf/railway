package uz.pdp.railway.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.railway.model.entity.RoleEntity;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByAuthority(String authority);
}
