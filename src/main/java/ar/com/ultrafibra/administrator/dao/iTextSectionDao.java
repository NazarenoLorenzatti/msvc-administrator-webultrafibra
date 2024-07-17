package ar.com.ultrafibra.administrator.dao;

import ar.com.ultrafibra.administrator.entities.TextSection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTextSectionDao extends JpaRepository<TextSection, Long>{
    
}
