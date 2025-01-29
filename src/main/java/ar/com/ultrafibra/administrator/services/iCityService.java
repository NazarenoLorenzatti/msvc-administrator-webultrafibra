package ar.com.ultrafibra.administrator.services;

import ar.com.ultrafibra.administrator.responses.CityResponseRest;
import org.springframework.http.ResponseEntity;

public interface iCityService {
    
     public ResponseEntity<CityResponseRest> getCities();
}
