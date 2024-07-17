package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.iImgSectionDao;
import ar.com.ultrafibra.administrator.entities.ImgSection;
import ar.com.ultrafibra.administrator.services.iObsService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
public class ObsServiceImpl implements iObsService {

    private final String AK = "ZEN8YOXYCQBKP3KBW1YH";
    private final String SK = "lH38DmKWYJ0JMZCSNKhtDxIbPMpU94bplqIZNklo";
    private final String END_POINT = "https://obs.sa-argentina-1.myhuaweicloud.com";
    private final String BUCKETNAME = "ultra-storage";

    @Autowired
    private iImgSectionDao imgDao;

    public String putObsObject(MultipartFile img, String accessKey) throws IOException {
        ObsClient obsClient = new ObsClient(AK, SK, END_POINT);
        try {
            obsClient.putObject(this.BUCKETNAME, accessKey, new ByteArrayInputStream(img.getBytes()));
            System.out.println("putObject successfully");
            obsClient.close();
            return generateSignedUrl(accessKey);
        } catch (ObsException e) {
            System.out.println("putObject failed");
            System.out.println("HTTP Code:" + e.getResponseCode());
            System.out.println("Error Code:" + e.getErrorCode());
            System.out.println("Error Message:" + e.getErrorMessage());
            System.out.println("Request ID:" + e.getErrorRequestId());
            System.out.println("Host ID:" + e.getErrorHostId());
            obsClient.close();
            return "No se cargo el archivo o no se consiguio el url";
        } catch (IOException e) {
            System.out.println("putObject failed");
            return "No se cargo el archivo o no se consiguio el url";
        }
    }

    public String generateSignedUrl(String accessKey) throws IOException {
        ObsClient obsClient = new ObsClient(AK, SK, END_POINT);
        try {
            long expireSeconds = 21600L;
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            request.setBucketName("ultra-storage");
            request.setObjectKey(accessKey);
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            System.out.println("Getting object using temporary signature url:");
            System.out.println("SignedUrl:" + response.getSignedUrl());
            obsClient.close();
            return response.getSignedUrl();
        } catch (ObsException e) {
            System.out.println("getObject failed");
            System.out.println("HTTP Code:" + e.getResponseCode());
            System.out.println("Error Code:" + e.getErrorCode());
            System.out.println("Error Message:" + e.getErrorMessage());
            System.out.println("Request ID:" + e.getErrorRequestId());
            System.out.println("Host ID:" + e.getErrorHostId());
            obsClient.close();
            return "Error al Obtener el URL";
        } catch (IOException ex) {
            Logger.getLogger(ObsServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return "Error al Obtener el URL";
        }
    }

    @Async
    @Scheduled(fixedDelay = 19800L)
    public void renewSignedUrl() throws IOException {
        for (ImgSection img : imgDao.findAll()) {
            String newUrl = this.generateSignedUrl(img.getAccessKey());
            img.setUrlObs(newUrl);
            imgDao.save(img);
        }
    }
}
