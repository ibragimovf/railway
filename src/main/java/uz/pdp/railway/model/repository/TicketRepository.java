package uz.pdp.railway.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.railway.model.entity.SeatEntity;
import uz.pdp.railway.model.entity.TicketEntity;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByUserId(long user_id);
}
