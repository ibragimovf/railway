package uz.pdp.railway.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.railway.model.entity.CitiesEntity;
import uz.pdp.railway.model.entity.TrainEntity;

import java.sql.Timestamp;
import java.util.List;

public interface TrainRepository extends JpaRepository<TrainEntity, Long> {
    List<TrainEntity> findByFromLocationAndToLocationAndDepartureDate(CitiesEntity formLocation, CitiesEntity toLocation, Timestamp departureDate);

    @Query(value = "select * from train_entity t inner join train_entity_seats ts on t.id = ts.train_entity_id inner join ticket_entity tc on tc.seat_id = ts.seats_id  where tc.user_id = ?1", nativeQuery = true)
    List<TrainEntity> findByUserId(long userId);

    @Query(value = " select * "+
            " from train_entity t " +
            " inner join train_entity_seats ts on t.id = ts.train_entity_id " +
            " inner join cities_entity c on c.id = t.from_location_id " +
            " inner join cities_entity ci on ci.id = t.to_location_id " +
            " inner join seat_entity se on se.id = ts.seats_id " +
            " inner join ticket_entity tc on tc.seat_id = ts.seats_id " +
            "where tc.user_id = ?1", nativeQuery = true)
    List<TrainEntity> findAllByUserId(long id);

//    @Query(value = "select * from train_entity t inner join cities_entity ce on t.form_location_id = ce.id inner join cities_entity c on t.to_location_id = c.id where ce.id = ?1 and c.id = ?2 and between ?3 and ?4", nativeQuery = true)
//    List<TrainEntity> getByFromLocationAndToLocationAndDepartureDate(long fromLocation, long toLocation, String date, String date2);

    List<TrainEntity> getTrainEntitiesByFromLocationAndToLocationAndDepartureDateBetween(CitiesEntity fromLocation, CitiesEntity toLocation, Timestamp departureDate, Timestamp departureDate2);
}
