package uz.pdp.railway.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainDto {
    private String name;
    private Long fromLocation;
    private Long toLocation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;
    private int localHours;
    private int localMinutes;
    private int hours;
    private int minutes;
    private List<String> classes;
    private int platscard;
    private int general;
    private int coupe;
    private int sedentary;
    private int sv;
}
