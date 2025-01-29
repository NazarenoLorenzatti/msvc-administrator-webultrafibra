package ar.com.ultrafibra.administrator.services.impl;

import ar.com.ultrafibra.administrator.dao.*;
import ar.com.ultrafibra.administrator.entities.*;
import ar.com.ultrafibra.administrator.responses.SectionsWebResponseRest;
import ar.com.ultrafibra.administrator.services.iAdministratorService;
import java.io.IOException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdministratorServiceImpl implements iAdministratorService {

    private final iSectionsWebDao sectionsDao;
    private final iImgSectionDao imgDao;
    private final ObsServiceImpl obsService;

    @Override
    public ResponseEntity<SectionsWebResponseRest> getSection(Long id) {
        return getSectionResponse(sectionsDao.findById(id), "ID");
    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> getSection(String accessKey) {
        return getSectionResponse(sectionsDao.findByAccessKey(accessKey), "accessKey");
    }

    private ResponseEntity<SectionsWebResponseRest> getSectionResponse(Optional<SectionsWeb> optional, String criteria) {
        try {
            return optional.map(section -> buildResponse(Collections.singletonList(section), HttpStatus.OK, "Sección encontrada en BBDD"))
                    .orElseGet(() -> buildResponse(null, HttpStatus.NOT_FOUND, "No se encuentra ninguna sección con este " + criteria));
        } catch (Exception e) {
            log.error("Error al consultar sección por " + criteria, e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar");
        }
    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> editSectionText(SectionsWeb section) {
        try {
            Optional<SectionsWeb> optional = sectionsDao.findByAccessKey(section.getAccessKey());
            if (optional.isEmpty()) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "No se encuentra ninguna sección con este ID");
            }

            SectionsWeb existingSection = optional.get();
            updateTextSections(existingSection, section.getTexts());
            sectionsDao.save(existingSection);

            return buildResponse(Collections.singletonList(existingSection), HttpStatus.OK, "Sección actualizada en BBDD");
        } catch (Exception e) {
            log.error("Error al actualizar sección", e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar");
        }
    }

    private void updateTextSections(SectionsWeb existingSection, List<TextSection> newTexts) {
        existingSection.getTexts().forEach(existingText -> 
            newTexts.stream()
                    .filter(newText -> newText.getAccessKey().equals(existingText.getAccessKey()))
                    .findFirst()
                    .ifPresent(newText -> {
                        existingText.setText(newText.getText());
                        existingText.setDescription(newText.getDescription());
                    })
        );
    }

    @Override
    public ResponseEntity<SectionsWebResponseRest> editSectionImg(String sectionAccessKey, MultipartFile file, String accessKeyImg, String description) {
        try {
            Optional<SectionsWeb> optionalSection = sectionsDao.findByAccessKey(sectionAccessKey);
            if (optionalSection.isEmpty()) {
                return buildResponse(null, HttpStatus.NOT_FOUND, "No se encuentra ninguna sección con este ID");
            }

            SectionsWeb section = optionalSection.get();
            updateOrSaveImage(section, file, accessKeyImg, description);

            SectionsWeb updatedSection = sectionsDao.findByAccessKey(sectionAccessKey).orElse(section);
            return buildResponse(Collections.singletonList(updatedSection), HttpStatus.OK, "Imágenes de la sección actualizadas en BBDD");
        } catch (Exception e) {
            log.error("Error al actualizar imagen de la sección", e);
            return buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar");
        }
    }

    private void updateOrSaveImage(SectionsWeb section, MultipartFile file, String accessKeyImg, String description) throws IOException {
        String signedUrl = obsService.putObsObject(file, accessKeyImg);
        Optional<ImgSection> optionalImg = imgDao.findByAccessKey(accessKeyImg);

        ImgSection img = optionalImg.orElseGet(ImgSection::new);
        img.setSectionWeb(section);
        img.setUrlObs(signedUrl);
        img.setDescription(description);
        img.setAccessKey(accessKeyImg);
        imgDao.save(img);
    }

    private ResponseEntity<SectionsWebResponseRest> buildResponse(List<SectionsWeb> sections, HttpStatus status, String message) {
        SectionsWebResponseRest response = new SectionsWebResponseRest();
        response.getSectionsWebResponse().setSectionsWeb(sections != null ? sections : Collections.emptyList());
        response.setMetadata("Respuesta " + (status.is2xxSuccessful() ? "ok" : "nok"),
                             status == HttpStatus.OK ? "00" : "-1",
                             message);
        return new ResponseEntity<>(response, status);
    }
}
