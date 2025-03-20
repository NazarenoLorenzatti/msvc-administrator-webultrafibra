package ar.com.ultrafibra.administrator.controller;

import ar.com.ultrafibra.administrator.entities.SectionsWeb;
import ar.com.ultrafibra.administrator.responses.*;
import ar.com.ultrafibra.administrator.services.iAdministratorService;
import ar.com.ultrafibra.administrator.services.iCityService;
import ar.com.ultrafibra.administrator.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {
    "http://119.8.72.246",
    "https://119.8.72.246",
    "https://ultrafibra.com.ar",
    "*"})
@RequestMapping("/api/uf")
public class AdminController {

    @Autowired
    private iAdministratorService adminService;

    @Autowired
    private iCityService cityService;

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

    @PutMapping(path = "/edit-text")
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
