package uz.pdp.railway.service;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import uz.pdp.railway.model.dao.TrainsDao;
import uz.pdp.railway.model.dto.TrainDto;
import uz.pdp.railway.model.entity.CitiesEntity;
import uz.pdp.railway.model.entity.SeatEntity;
import uz.pdp.railway.model.entity.TrainEntity;
import uz.pdp.railway.model.entity.UserEntity;
import uz.pdp.railway.model.repository.TrainRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TrainService {
    private final TrainRepository trainRepository;
    private final CitiesService citiesService;
    private final SeatService seatService;

    public TrainService(TrainRepository trainRepository, CitiesService citiesService, SeatService seatService) {
        this.trainRepository = trainRepository;
        this.citiesService = citiesService;
        this.seatService = seatService;
    }

    public List<TrainEntity> getDistinctTrainList(CitiesEntity fromLocation, CitiesEntity toLocation, LocalDate localDate) {
        Timestamp timestamp = new Timestamp(localDate.toDateTimeAtStartOfDay().getMillis());
        Timestamp timestamp2 = new Timestamp(localDate.plusDays(1).toDateTimeAtStartOfDay().getMillis());
        List<TrainEntity> trainEntityList = trainRepository.getTrainEntitiesByFromLocationAndToLocationAndDepartureDateBetween(fromLocation, toLocation, timestamp, timestamp2);
        List<TrainEntity> list = new ArrayList<>();
        for (TrainEntity train : trainEntityList) {
            train.setSeats(train.getSeats().stream().peek(seatEntity -> seatEntity.setId(1)).distinct().toList());
            if (train.getArrivalDate().after(Timestamp.from(Instant.now()))) {
                list.add(train);
            }
        }
        return list;
    }

    public TrainEntity getById(Optional<Long> trainId) {
        if (trainId.isPresent()) {
            Optional<TrainEntity> byId = trainRepository.findById(trainId.get());
            return byId.orElse(null);
        }
        return null;
    }

    public String info(TrainEntity train) {
        long interval = train.getArrivalDate().getTime() - train.getDepartureDate().getTime();
        String format = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toHours(interval),
                TimeUnit.MILLISECONDS.toMinutes(interval) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(interval))
        );
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm - dd.MM.yyyy");
        return "KETISH: " + formatter.format(train.getDepartureDate()) +
                " " + train.getFromLocation().getName() +
                ", SAYOHAT VAQTI: " + format + " " +
                ", KELISH: " + formatter.format(train.getArrivalDate()) +
                " " + train.getToLocation().getName();
    }

    public List<TrainEntity> getByUserId(UserEntity userEntity) {
        return trainRepository.findAllByUserId(userEntity.getId());
    }

    public boolean addTrain(TrainDto trainDto) {
        TrainEntity train = new TrainEntity();
        List<SeatEntity> entityList = seatService.addAndGetSeatList(trainDto);
        train.setName(trainDto.getName());
        train.setFromLocation(citiesService.getById(trainDto.getFromLocation()));
        train.setToLocation(citiesService.getById(trainDto.getToLocation()));
        train.setSeats(entityList);
        train.setDepartureDate(getTimestamp(trainDto));
        train.setArrivalDate(getArrivalTimestamp(trainDto));
        train.setTotalSeat(entityList.size());
        trainRepository.save(train);
        return true;
    }

    private Timestamp getArrivalTimestamp(TrainDto trainDto) {
        Calendar calendar = getCalendar(trainDto);
        calendar.add(Calendar.HOUR_OF_DAY, trainDto.getHours());
        calendar.add(Calendar.MINUTE, trainDto.getMinutes());
        return new Timestamp(calendar.getTimeInMillis());
    }

    private Calendar getCalendar(TrainDto trainDto) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trainDto.getDepartureDate());
        calendar.set(Calendar.HOUR_OF_DAY, trainDto.getLocalHours());
        calendar.set(Calendar.MINUTE, trainDto.getLocalMinutes());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private Timestamp getTimestamp(TrainDto trainDto) {
        Calendar calendar = getCalendar(trainDto);
        return new Timestamp(calendar.getTimeInMillis());
    }
}
