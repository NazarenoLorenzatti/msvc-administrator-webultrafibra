package ar.com.ultrafibra.administrator.services;

import org.springframework.web.multipart.MultipartFile;

public interface iObsService {
    
    public String putObsObject(MultipartFile img, String objectKey);
    
    public String generateSignedUrl(String objectKey);
    
    public void renewSignedUrls();
}
