package ar.com.ultrafibra.administrator.dao;

import ar.com.ultrafibra.administrator.entities.Cities;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iCitiesDao extends JpaRepository<Cities, Long>{
    
    public Optional<Cities> findByCity(String city);
    
}
