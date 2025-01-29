package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.iCitiesDao;
import ar.com.ultrafibra.administrator.entities.Cities;
import ar.com.ultrafibra.administrator.responses.CityResponseRest;
import ar.com.ultrafibra.administrator.services.iCityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements iCityService {

    private final iCitiesDao cityDao;

    @Override
    public ResponseEntity<CityResponseRest> getCities() {
        try {
            List<Cities> cities = cityDao.findAll();
            return cities.isEmpty()
                    ? buildResponse(null, HttpStatus.NOT_FOUND, "No se encontró ninguna ciudad")
                    : buildResponse(cities, HttpStatus.OK, "Lista de localidades encontrada");
        } catch (Exception e) {
            log.error("Error al consultar ciudades", e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar");
        }
    }

    private ResponseEntity<CityResponseRest> buildResponse(List<Cities> cities, HttpStatus status, String message) {
        CityResponseRest response = new CityResponseRest();
        if (cities != null) {
            response.getCitiesResponse().setCities(cities);
        }
        response.setMetadata("Respuesta " + (status.is2xxSuccessful() ? "ok" : "nok"), 
                             status == HttpStatus.OK ? "00" : "-1", 
                             message);
        return new ResponseEntity<>(response, status);
    }
}