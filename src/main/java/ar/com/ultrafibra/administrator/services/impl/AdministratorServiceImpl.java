package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.iImgSectionDao;
import ar.com.ultrafibra.administrator.dao.iSectionsWebDao;
import ar.com.ultrafibra.administrator.entities.ImgSection;
import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import ar.com.ultrafibra.administrator.entities.TextSection;
import ar.com.ultrafibra.administrator.responses.SectionsWebResponseRest;
import ar.com.ultrafibra.administrator.services.iAdministratorService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Data
public class AdministratorServiceImpl implements iAdministratorService {

    @Autowired
    private iSectionsWebDao sectionsDao;

    @Autowired
    private iImgSectionDao imgDao;

    @Autowired
    private ObsServiceImpl obsService;

    @Override
    public ResponseEntity<SectionsWebResponseRest> getSection(Long id) {
        SectionsWebResponseRest respuesta = new SectionsWebResponseRest();
        Optional<SectionsWeb> optional = sectionsDao.findById(id);
        List<SectionsWeb> list = new ArrayList();
        try {
            if (optional.isPresent()) {
                list.add(optional.get());
                respuesta.getSectionsWebResponse().setSectionsWeb(list);
                respuesta.setMetadata("Respuesta ok", "00", "Seccion Encontrada en BBDD");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra Ninguna seccion con este ID");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> getSection(String accessKey) {
        SectionsWebResponseRest respuesta = new SectionsWebResponseRest();
        Optional<SectionsWeb> optional = sectionsDao.findByAccessKey(accessKey);
        List<SectionsWeb> list = new ArrayList();
        try {
            if (optional.isPresent()) {
                list.add(optional.get());
                respuesta.getSectionsWebResponse().setSectionsWeb(list);
                respuesta.setMetadata("Respuesta ok", "00", "Seccion Encontrada en BBDD");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra Ninguna seccion con este ID");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> editSectionText(SectionsWeb section) {
        SectionsWebResponseRest respuesta = new SectionsWebResponseRest();
        Optional<SectionsWeb> optional = sectionsDao.findByAccessKey(section.getAccessKey());
        List<SectionsWeb> list = new ArrayList();
        try {
            if (optional.isPresent()) {
                SectionsWeb s = optional.get();
                for (TextSection textModify : section.getTexts()) {
                    for (TextSection textBBDD : s.getTexts()) {
                        if(textBBDD.getAccessKey().equals(textModify.getAccessKey())){
                            textBBDD.setText(textModify.getText());
                            textBBDD.setDescription(textModify.getDescription());
                        }
                    }
                }
                sectionsDao.save(s);
                list.add(s);
                respuesta.getSectionsWebResponse().setSectionsWeb(list);
                respuesta.setMetadata("Respuesta ok", "00", "Seccion Encontrada en BBDD");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra Ninguna seccion con este ID");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> editSectionImg(String sectionAccesKey, MultipartFile file, String accessKeyImg, String description) {
        SectionsWebResponseRest respuesta = new SectionsWebResponseRest();
        Optional<SectionsWeb> optional = sectionsDao.findByAccessKey(sectionAccesKey);
        List<SectionsWeb> list = new ArrayList();
        try {
            if (optional.isPresent()) {
                SectionsWeb s = optional.get();
                Optional<ImgSection> optionalImg = imgDao.findByAccessKey(accessKeyImg);
                if (optionalImg.isPresent()) {
                    // Si la imagen se encuentra en base de datos se reemplaza
                    ImgSection img = optionalImg.get();
                    String signedUrl = obsService.putObsObject(file, accessKeyImg);
                    img.setUrlObs(signedUrl);
                    imgDao.save(img);
                } else {
                    //Si la imagen no se encuentra en base de datos se agrega una nueva imagen relacionada a la seccion a la BBDD
                    String signedUrl = obsService.putObsObject(file, accessKeyImg);
                    ImgSection img = new ImgSection();
                    img.setSectionWeb(s);
                    img.setUrlObs(signedUrl);
                    img.setDescription(description);
                    img.setAccessKey(accessKeyImg);
                    imgDao.save(img);
                }
                list.add(sectionsDao.findByAccessKey(sectionAccesKey).get());
                respuesta.getSectionsWebResponse().setSectionsWeb(list);
                respuesta.setMetadata("Respuesta ok", "00", "Imagenes de la seecion actualizadas en BBDD");
                return new ResponseEntity<>(respuesta, HttpStatus.OK);
            } else {
                respuesta.setMetadata("Respuesta nok", "-1", "No se encuentra Ninguna seccion con este ID");
                return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            respuesta.setMetadata("Respuesta nok", "-1", "Error al consultar");
            e.getStackTrace();
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
