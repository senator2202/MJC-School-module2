package com.epam.esm.service;

import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

/**
 * Interface provides some additional operations on GiftCertificate entity
 */
public interface GiftCertificateService extends BaseService<GiftCertificate> {

    /**
     * Find all list. Filter and sort entities, according to input parameters
     *
     * @param name        the name
     * @param description the description
     * @param tagName     the tag name
     * @param sortType    the sort type
     * @param direction   the direction
     * @return the list
     */
    List<GiftCertificate> findAll(String name, String description, String tagName, String sortType, String direction);
}
