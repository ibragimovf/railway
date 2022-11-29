package uz.pdp.railway.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.railway.model.enums.TrainClassEnum;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated
    private TrainClassEnum aClass;
    private double price;
    private boolean isBusy;

    public SeatEntity(TrainClassEnum aClass, double price, boolean isBusy) {
        this.aClass = aClass;
        this.price = price;
        this.isBusy = isBusy;
    }
}
