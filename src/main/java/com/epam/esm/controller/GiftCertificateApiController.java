package com.epam.esm.controller;

import com.epam.esm.controller.exception.GiftEntityNotFoundException;
import com.epam.esm.controller.exception.WrongParameterFormatException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.GiftEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GiftCertificate api controller.
 */
@RestController
@RequestMapping("api/certificates")
public class GiftCertificateApiController {

    private GiftCertificateService service;

    @Autowired
    public void setService(GiftCertificateService service) {
        this.service = service;
    }

    /**
     * End point for findAllGiftCertificates request.
     * Optionally could be used with different parameters for filtering and sorting.
     */
    @GetMapping
    public List<GiftCertificate> findAll(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "description", required = false) String description,
                                         @RequestParam(value = "tag", required = false) String tagName,
                                         @RequestParam(value = "sort", required = false) String sortType,
                                         @RequestParam(value = "direction", required = false) String direction) {
        if (!GiftEntityValidator.correctOptionalParameters(name, description, tagName, sortType, direction)) {
            throw new WrongParameterFormatException("Wrong optional parameters", ErrorCode.WRONG_OPTIONAL_PARAMETERS);
        }
        return service.findAll(name, description, tagName, sortType, direction);
    }

    /**
     * End point for finding gift certificate by id request.
     */
    @GetMapping("/{id:^[1-9]\\d{0,18}$}")
    public GiftCertificate findById(@PathVariable long id) {
        return service.findById(id)
                .orElseThrow(() ->
                        new GiftEntityNotFoundException("Certificate not found", ErrorCode.GIFT_CERTIFICATE_NOT_FOUND));
    }

    /**
     * End point for adding new gift certificate request.
     */
    @PostMapping
    public GiftCertificate create(@RequestBody GiftCertificate certificate) {
        return service.add(certificate);
    }

    /**
     * End point for updating gift certificate request.
     */
    @PutMapping("/{id:^[1-9]\\d{0,18}$}")
    public GiftCertificate update(@RequestBody GiftCertificate certificate, @PathVariable long id) {
        certificate.setId(id);
        return service.update(certificate)
                .orElseThrow(() ->
                        new GiftEntityNotFoundException("Certificate not found", ErrorCode.GIFT_CERTIFICATE_NOT_FOUND));
    }

    /**
     * End point for deleting gift certificate request.
     */
    @DeleteMapping("/{id:^[1-9]\\d{0,18}$}")
    public Boolean delete(@PathVariable long id) {
        return service.delete(id);
    }
}
