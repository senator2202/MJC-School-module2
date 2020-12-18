package com.epam.esm.controller;

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
        return service.findById(id);
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
        return service.update(certificate).orElse(null);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
