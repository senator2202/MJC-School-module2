package com.epam.esm.controller;

import com.epam.esm.controller.exception.GiftEntityNotFoundException;
import com.epam.esm.controller.exception.WrongParameterFormatException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftEntityValidator;
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

    @GetMapping("/{id:^[1-9]\\d{0,18}$}")
    @ResponseBody
    public GiftCertificate findById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() ->
                        new GiftEntityNotFoundException("Certificate not found", ErrorCode.GIFT_CERTIFICATE_NOT_FOUND));
    }

    @PostMapping("/")
    @ResponseBody
    public GiftCertificate create(@RequestBody GiftCertificate certificate) {
        return service.add(certificate);
    }

    @PutMapping("/{id:^[1-9]\\d{0,18}$}")
    @ResponseBody
    public GiftCertificate update(@RequestBody GiftCertificate certificate, @PathVariable long id) {
        certificate.setId(id);
        return service.update(certificate)
                .orElseThrow(() ->
                        new GiftEntityNotFoundException("Certificate not found", ErrorCode.GIFT_CERTIFICATE_NOT_FOUND));
    }

    @DeleteMapping("/{id:^[1-9]\\d{0,18}$}")
    @ResponseBody
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @GetMapping("/find/tag/{tagName}")
    @ResponseBody
    public List<GiftCertificate> findByTagName(@PathVariable String tagName,
                                               @RequestParam(value = "sort", required = false) String sortType,
                                               @RequestParam(value = "direction", required = false) String direction) {
        if (!GiftEntityValidator.correctTagName(tagName)) {
            throw new WrongParameterFormatException("Wrong tag name format", ErrorCode.NAME_WRONG_FORMAT);
        }
        return service.findByTagName(tagName, sortType, direction);
    }

    @GetMapping("/find/name/{name}")
    @ResponseBody
    public List<GiftCertificate> findByName(@PathVariable String name,
                                            @RequestParam(value = "sort", required = false) String sortType,
                                            @RequestParam(value = "direction", required = false) String direction) {
        if (!GiftEntityValidator.correctTagName(name)) {
            throw new WrongParameterFormatException("Wrong certificate name format", ErrorCode.NAME_WRONG_FORMAT);
        }
        return service.findByName(name, sortType, direction);
    }

    @GetMapping("/find/description/{description}")
    @ResponseBody
    public List<GiftCertificate> findByDescription(@PathVariable String description,
                                                   @RequestParam(value = "sort", required = false) String sortType,
                                                   @RequestParam(value = "direction", required = false) String direction) {
        if (!GiftEntityValidator.correctCertificateDescription(description)) {
            throw new WrongParameterFormatException("Wrong certificate name format", ErrorCode.DESCRIPTION_WRONG_FORMAT);
        }
        return service.findByDescription(description, sortType, direction);
    }
}
