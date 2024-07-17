package ar.com.ultrafibra.administrator.services;

import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import ar.com.ultrafibra.administrator.responses.SectionsWebResponseRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface iAdministratorService {
    
     public ResponseEntity<SectionsWebResponseRest> getSection(Long id);
     public ResponseEntity<SectionsWebResponseRest> getSection(String accessKey);
     public ResponseEntity<SectionsWebResponseRest> editSectionText(SectionsWeb section);
     public ResponseEntity<SectionsWebResponseRest> editSectionImg(String sectionAccesKey, MultipartFile file, String accessKey, String description);
}
