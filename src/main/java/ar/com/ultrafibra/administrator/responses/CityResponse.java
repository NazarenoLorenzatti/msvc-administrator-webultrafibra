package ar.com.ultrafibra.administrator.responses;

import ar.com.ultrafibra.administrator.entities.Cities;
import java.util.List;
import lombok.Data;

@Data
public class CityResponse {
    
    private List<Cities> cities;
}
