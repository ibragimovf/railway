package uz.pdp.railway.service;

import org.springframework.stereotype.Service;
import uz.pdp.railway.model.dto.TrainDto;
import uz.pdp.railway.model.entity.SeatEntity;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.model.enums.TrainClassEnum;
import uz.pdp.railway.model.repository.SeatRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatEntity getById(Optional<Long> seatId) {
        if (seatId.isPresent()) {
            Optional<SeatEntity> byId = seatRepository.findById(seatId.get());
            return byId.orElse(null);
        }
        return null;
    }

    public void seatBusy(SeatEntity seatEntity) {
        seatEntity.setBusy(true);
        seatRepository.save(seatEntity);
    }

    public List<SeatEntity> addAndGetSeatList(TrainDto trainDto) {
        List<SeatEntity> list = getList(trainDto);
        seatRepository.saveAll(list);
        return list;
    }

    private List<SeatEntity> getList(TrainDto trainDto) {
        List<SeatEntity> entityList = new ArrayList<>();
        for (String aClass : trainDto.getClasses()) {
            if (aClass.equals(TrainClassEnum.PLATSCARD.name())) {
                for (int i = 0; i < trainDto.getPlatscard(); i++) {
                    entityList.add(new SeatEntity(TrainClassEnum.PLATSCARD, 5, false));
                }
            } else if (aClass.equals(TrainClassEnum.GENERAL.name())) {
                for (int i = 0; i < trainDto.getGeneral(); i++) {
                    entityList.add(new SeatEntity(TrainClassEnum.GENERAL, 10, false));
                }
            } else if (aClass.equals(TrainClassEnum.COUPE.name())) {
                for (int i = 0; i < trainDto.getCoupe(); i++) {
                    entityList.add(new SeatEntity(TrainClassEnum.COUPE, 15, false));
                }
            } else if (aClass.equals(TrainClassEnum.SEDENTARY.name())) {
                for (int i = 0; i < trainDto.getSedentary(); i++) {
                    entityList.add(new SeatEntity(TrainClassEnum.SEDENTARY, 20, false));
                }
            } else if (aClass.equals(TrainClassEnum.SV.name())) {
                for (int i = 0; i < trainDto.getSv(); i++) {
                    entityList.add(new SeatEntity(TrainClassEnum.SV, 25, false));
                }
            }
        }
        return entityList;
    }
}
