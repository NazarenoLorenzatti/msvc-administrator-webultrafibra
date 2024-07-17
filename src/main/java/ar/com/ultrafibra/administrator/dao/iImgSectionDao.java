package ar.com.ultrafibra.administrator.dao;

import ar.com.ultrafibra.administrator.entities.ImgSection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iImgSectionDao extends JpaRepository<ImgSection, Long>{
    
    public Optional<ImgSection> findByAccessKey(String accessKey);
}
