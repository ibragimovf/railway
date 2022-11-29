package uz.pdp.railway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectTicketDto {
    private long fromLocation;
    private long toLocation;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dates;

}
