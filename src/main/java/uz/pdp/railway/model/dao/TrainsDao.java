package uz.pdp.railway.model.dao;

import lombok.*;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class TrainsDao {
    private String trainName;
    private String fromCitiesName;
    private String toCitiesName;
    private Timestamp departureDate;
    private Timestamp arrivalDate;
    private Long seatId;
    private Integer aClass;
    private Double price;
}
