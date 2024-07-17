package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.iCitiesDao;
import ar.com.ultrafibra.administrator.entities.Cities;
import ar.com.ultrafibra.administrator.responses.CityResponseRest;
import ar.com.ultrafibra.administrator.services.iCityService;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Data
public class CityServiceImpl implements iCityService {

    @Autowired
    private iCitiesDao cityDao;

    @Override
    public ResponseEntity<CityResponseRest> getCity(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ResponseEntity<CityResponseRest> getCities() {
        CityResponseRest respuesta = new CityResponseRest();
        List<Cities> list = cityDao.findAll();
        try {
            if (!list.isEmpty()) {
                respuesta.getCitiesResponse().setCities(list);
                respuesta.setMetadata("Respuesta ok", "00", "Lista de localidades encontrada");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encontro ninguna ciudad");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
