package com.epam.esm.model.dao;

import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

/**
 * Interface provides provides additional operation for gift certificates
 */
public interface GiftCertificateDao extends BaseDao<GiftCertificate> {

    /**
     * Find all gift certificates, matching input parameters
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
