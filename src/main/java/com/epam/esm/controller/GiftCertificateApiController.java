package com.epam.esm.controller;

import com.epam.esm.controller.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/certificates")
public class GiftCertificateApiController {

    private GiftCertificateService service;

    @Autowired
    public void setService(GiftCertificateService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public List<GiftCertificate> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public GiftCertificate findById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() ->
                        new GiftCertificateNotFoundException("Gift certificate not found (id =" + id + ")"));
    }

    @PostMapping("/")
    @ResponseBody
    public GiftCertificate create(@RequestBody GiftCertificate certificate) {
        return service.add(certificate);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public GiftCertificate update(@RequestBody GiftCertificate certificate, @PathVariable long id) {
        certificate.setId(id);
        return service.update(certificate)
                .orElseThrow(() ->
                        new GiftCertificateNotFoundException("Gift certificate not found (id =" + id + ")"));
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/find/tag/{tagName}")
    @ResponseBody
    public List<GiftCertificate> findByTagName(@PathVariable String tagName,
                                               @RequestParam(value = "sort", required = false) String sortType,
                                               @RequestParam(value = "direction", required = false) String direction) {
        return service.findByTagName(tagName, sortType, direction);
    }

    @GetMapping("/find/name/{name}")
    @ResponseBody
    public List<GiftCertificate> findByName(@PathVariable String name,
                                            @RequestParam(value = "sort", required = false) String sortType,
                                            @RequestParam(value = "direction", required = false) String direction) {
        return service.findByName(name, sortType, direction);
    }

    @GetMapping("/find/description/{description}")
    @ResponseBody
    public List<GiftCertificate> findByDescription(@PathVariable String description,
                                                   @RequestParam(value = "sort", required = false) String sortType,
                                                   @RequestParam(value = "direction", required = false) String direction) {
        return service.findByDescription(description, sortType, direction);
    }
}
