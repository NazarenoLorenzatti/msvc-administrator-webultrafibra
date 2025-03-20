package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.iImgSectionDao;
import ar.com.ultrafibra.administrator.services.iObsService;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.HttpMethodEnum;
import com.obs.services.model.TemporarySignatureRequest;
import com.obs.services.model.TemporarySignatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class ObsServiceImpl implements iObsService {

    @Value("${app.ak}")
    private String accessKey;

    @Value("${app.sk}")
    private String secretKey;

    @Value("${app.endpoint}")
    private String endPoint;

    @Value("${app.bucketname}")
    private String bucketName;

    @Autowired
    private iImgSectionDao imgDao;

    @Override
    public String putObsObject(MultipartFile img, String objectKey) {
        try ( ObsClient obsClient = createObsClient()) {
            obsClient.putObject(bucketName, objectKey, new ByteArrayInputStream(img.getBytes()));
            log.info("Archivo subido con éxito a OBS: {}", objectKey);
            return generateSignedUrl(objectKey);
        } catch (ObsException e) {
            log.error("Error al subir objeto a OBS - HTTP Code: {}, Error Code: {}, Message: {}",
                    e.getResponseCode(), e.getErrorCode(), e.getErrorMessage(), e);
        } catch (IOException e) {
            log.error("Error de I/O al subir el objeto a OBS", e);
        }
        return "No se pudo cargar el archivo o generar la URL.";
    }

    @Override
    public String generateSignedUrl(String objectKey) {
        try ( ObsClient obsClient = createObsClient()) {
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, 21600L);
            request.setBucketName(bucketName);
            request.setObjectKey(objectKey);
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            log.info("URL firmada generada con éxito: {}", response.getSignedUrl());
            return response.getSignedUrl();
        } catch (ObsException e) {
            log.error("Error al generar URL firmada - HTTP Code: {}, Error Code: {}, Message: {}",
                    e.getResponseCode(), e.getErrorCode(), e.getErrorMessage(), e);
        } catch (IOException e) {
            log.error("Error de I/O al generar la URL firmada", e);
        }
        return "Error al obtener la URL firmada.";
    }

    @Async
    @Scheduled(fixedDelay = 600000L)
    @Override
    public void renewSignedUrls() {
        imgDao.findAll().forEach(img -> {
            String newUrl = generateSignedUrl(img.getAccessKey());
            img.setUrlObs(newUrl);
            imgDao.save(img);
            log.info("URL firmada actualizada para: {}", img.getAccessKey());
        });
    }

    private ObsClient createObsClient() {
        return new ObsClient(accessKey, secretKey, endPoint);
    }
}
