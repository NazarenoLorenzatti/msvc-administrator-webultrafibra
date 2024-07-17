package ar.com.ultrafibra.administrator.controller;

import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import ar.com.ultrafibra.administrator.responses.CityResponseRest;
import ar.com.ultrafibra.administrator.responses.SectionsWebResponseRest;
import ar.com.ultrafibra.administrator.services.impl.AdministratorServiceImpl;
import ar.com.ultrafibra.administrator.services.impl.CityServiceImpl;
import ar.com.ultrafibra.administrator.services.impl.ObsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {
    "*"})
@RequestMapping("/api/uf")
public class AdminController {

    @Autowired
    private AdministratorServiceImpl adminService;

    @Autowired
    private CityServiceImpl cityService;

    @GetMapping(path = "/get-section/id/{id}")
    public ResponseEntity<SectionsWebResponseRest> getSection(@PathVariable Long id) {
        return adminService.getSection(id);
    }

    @GetMapping(path = "/get-section/accesskey/{accessKey}")
    public ResponseEntity<SectionsWebResponseRest> getSection(@PathVariable String accessKey) {
        return adminService.getSection(accessKey);
    }

    @GetMapping(path = "/get-cities")
    public ResponseEntity<CityResponseRest> listCities() {
        return cityService.getCities();
    }

    @PutMapping(path = "im")
    public ResponseEntity<SectionsWebResponseRest> editText(@RequestBody SectionsWeb sectionsWeb) {
        return adminService.editSectionText(sectionsWeb);
    }

    @PutMapping(path = "/edit-img")
    public ResponseEntity<SectionsWebResponseRest> editImg(
            @RequestParam("file") MultipartFile file,
            @RequestParam("accesKeyImg") String accessKeyImg,
            @RequestParam("description") String description,
            @RequestParam("sectionAccesKey") String sectionAccesKey) throws Exception {
        return adminService.editSectionImg(sectionAccesKey, file, accessKeyImg, description);
    }
}
