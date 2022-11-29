package uz.pdp.railway.service;

import org.springframework.stereotype.Service;
import uz.pdp.railway.model.entity.SeatEntity;
import uz.pdp.railway.model.entity.TicketEntity;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.model.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final SeatService seatService;

    public TicketService(TicketRepository ticketRepository, SeatService seatService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
    }

    public void addTicket(Optional<Long> seatId, UserEntity userEntity) {
        SeatEntity seatEntity = seatService.getById(seatId);
        TicketEntity ticketEntity = new TicketEntity();
        ticketEntity.setSeat(seatEntity);
        ticketEntity.setUser(userEntity);
        seatService.seatBusy(seatEntity);
        ticketRepository.save(ticketEntity);
    }

    public List<TicketEntity> getByUserId(UserEntity userEntity) {
        return ticketRepository.findByUserId(userEntity.getId());
    }
}
