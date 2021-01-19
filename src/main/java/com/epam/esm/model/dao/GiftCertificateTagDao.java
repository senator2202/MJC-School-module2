package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;


/**
 * Interface provides data access for additional table in database, created because of
 * many-to-many link between GiftCertificate and Tag entities
 */
public interface GiftCertificateTagDao {
    /**
     * Add tag for gift certificate.
     *
     * @param certificateId the certificate id
     * @param tagId         the tag id
     */
    void add(long certificateId, long tagId);

    /**
     * Delete tag from gift certificate.
     *
     * @param certificateId the certificate id
     * @param tagId         the tag id
     */
    void delete(long certificateId, long tagId);

    /**
     * Delete all tags of gift certificate.
     *
     * @param certificateId the certificate id
     */
    void deleteAllTags(long certificateId);

    /**
     * Delete tag from all certificates.
     *
     * @param tagId the tag id
     */
    void deleteByTagId(long tagId);

    /**
     * Find all tags for certificate list.
     *
     * @param certificateId the certificate id
     * @return the list
     */
    List<Tag> findAllTagsForCertificate(long certificateId);

    /**
     * Certificate has tag boolean.
     *
     * @param certificateId the certificate id
     * @param tagId         the tag id
     * @return the boolean
     */
    boolean certificateHasTag(long certificateId, long tagId);
}
