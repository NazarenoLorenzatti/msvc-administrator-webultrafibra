package ar.com.ultrafibra.administrator.dao;

import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSectionsWebDao extends JpaRepository<SectionsWeb, Long>{
    
    public Optional<SectionsWeb> findByAccessKey(String accessKey);
}
