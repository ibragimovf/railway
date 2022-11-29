package uz.pdp.railway.service;

import org.springframework.stereotype.Service;
import uz.pdp.railway.model.dto.SelectTicketDto;
import uz.pdp.railway.model.entity.CitiesEntity;
import uz.pdp.railway.model.enums.CitiesEnum;
import uz.pdp.railway.model.repository.CitiesRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CitiesService {
    private final CitiesRepository citiesRepository;

    public CitiesService(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    public void addAll() {
        for (CitiesEnum cities : CitiesEnum.values()) {
            citiesRepository.save(new CitiesEntity(cities.name()));
        }
    }

    public void add(String citiName) {
        citiesRepository.save(new CitiesEntity(citiName));
    }

    public List<CitiesEntity> getList() {
        return citiesRepository.findAll();
    }

    public CitiesEntity getById(Optional<String> citiId) {
        return citiesRepository.findById(Long.parseLong(citiId.get())).get();
    }

    public CitiesEntity getById(long citiId) {
        return citiesRepository.findById(citiId).get();
    }

    public boolean equally(SelectTicketDto selectTicketDto) {
        return selectTicketDto.getFromLocation() == selectTicketDto.getToLocation();
    }

    public boolean isNull(SelectTicketDto selectTicketDto) {
        if (selectTicketDto.getFromLocation() == 0) {
            return true;
        } else if (selectTicketDto.getToLocation() == 0) {
            return true;
        } else return selectTicketDto.getDates() == null;
    }
}
