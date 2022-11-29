package uz.pdp.railway.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.railway.model.entity.CitiesEntity;

import java.util.Optional;

public interface CitiesRepository extends JpaRepository<CitiesEntity, Long> {
    Optional<CitiesEntity> findByName(String name);
}
