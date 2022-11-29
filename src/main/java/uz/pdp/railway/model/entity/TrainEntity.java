package uz.pdp.railway.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.railway.model.dao.TrainsDao;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
//@NamedNativeQuery(name = "TrainEntity.findAllByUserId",
//        query = " select t.name as train_name, " +
//                " c.name as from_cities_name, " +
//                " ci.name as to_cities_name, " +
//                " t.departure_date as departure_date, " +
//                " t.arrival_date as arrival_date, " +
//                " tc.seat_id as seat_id, " +
//                " se.a_class as a_class, " +
//                " se.price as price " +
//                " from train_entity t " +
//                " inner join train_entity_seats ts on t.id = ts.train_entity_id " +
//                " inner join cities_entity c on c.id = t.from_location_id " +
//                " inner join cities_entity ci on ci.id = t.to_location_id " +
//                " inner join seat_entity se on se.id = ts.seats_id " +
//                " inner join ticket_entity tc on tc.seat_id = ts.seats_id " +
//                "where tc.user_id = :id",
//        resultSetMapping = "TrainsDao")
//@SqlResultSetMapping(name = "TrainsDao",
//        classes = @ConstructorResult(targetClass = TrainsDao.class,
//                columns = {
//                        @ColumnResult(name = "train_name"),
//                        @ColumnResult(name = "from_cities_name"),
//                        @ColumnResult(name = "to_cities_name"),
//                        @ColumnResult(name = "departure_date"),
//                        @ColumnResult(name = "arrival_date"),
//                        @ColumnResult(name = "seat_id"),
//                        @ColumnResult(name = "a_class"),
//                        @ColumnResult(name = "price")
//                }
//        ))
@Entity
public class TrainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToOne
    private CitiesEntity fromLocation;

    @ManyToOne
    private CitiesEntity toLocation;

    @OneToMany
    private List<SeatEntity> seats;

    private Timestamp departureDate;

    private Timestamp arrivalDate;

    private int totalSeat;
}
