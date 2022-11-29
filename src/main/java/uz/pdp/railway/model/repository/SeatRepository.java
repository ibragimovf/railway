package uz.pdp.railway.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.railway.model.entity.SeatEntity;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatEntity, Long> {
}
