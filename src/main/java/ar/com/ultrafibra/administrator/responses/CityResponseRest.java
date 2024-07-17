package ar.com.ultrafibra.administrator.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityResponseRest extends ResponseRest {
    private CityResponse citiesResponse = new CityResponse();
}
