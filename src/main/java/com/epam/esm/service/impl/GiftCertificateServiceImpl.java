package com.epam.esm.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.GiftCertificateTagDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String DASH = "-";
    private static final String UNDER_SCOPE = "_";

    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private GiftCertificateTagDao giftCertificateTagDao;
    private TransactionTemplate transactionTemplate;

    public GiftCertificateServiceImpl() {
    }

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      TagDao tagDao,
                                      GiftCertificateTagDao giftCertificateTagDao,
                                      TransactionTemplate transactionTemplate) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
        this.giftCertificateTagDao = giftCertificateTagDao;
        this.transactionTemplate = transactionTemplate;
    }

    @Autowired
    public void setGiftCertificateDao(GiftCertificateDao giftCertificateDao) {
        this.giftCertificateDao = giftCertificateDao;
    }

    @Autowired
    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Autowired
    public void setGiftCertificateTagDao(GiftCertificateTagDao giftCertificateTagDao) {
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Autowired
    public void setPlatformTransactionManager(PlatformTransactionManager platformTransactionManager) {
        transactionTemplate = new TransactionTemplate(platformTransactionManager);
    }

    @Override
    public Optional<GiftCertificate> findById(long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    public List<GiftCertificate> findAll(String name,
                                         String description,
                                         String tagName,
                                         String sortType,
                                         String direction) {
        if (sortType != null) {
            sortType = sortType.replace(DASH, UNDER_SCOPE);
        }
        return giftCertificateDao.findAll(name, description, tagName, sortType, direction);
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        String currentDate = getCurrentDateIso();
        certificate.setCreateDate(currentDate);
        certificate.setLastUpdateDate(currentDate);
        return transactionTemplate.execute(transactionStatus -> {
            GiftCertificate added = giftCertificateDao.add(certificate);
            addTags(added, certificate.getTags());
            return added;
        });
    }

    private void addTags(GiftCertificate added, List<Tag> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            for (Tag tag : tags) {
                Tag addedTag = addTag(added.getId(), tag);
                added.addTag(addedTag);
            }
        }
    }

    private Tag addTag(long certificateId, Tag tag) {
        Optional<Tag> optionalTag = tagDao.findByName(tag.getName());
        Tag addedTag = optionalTag.orElseGet(() -> tagDao.add(tag));
        if (!giftCertificateTagDao.certificateHasTag(certificateId, addedTag.getId())) {
            giftCertificateTagDao.add(certificateId, addedTag.getId());
        }
        return addedTag;
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate certificate) {
        Optional<GiftCertificate> optional = giftCertificateDao.findById(certificate.getId());
        if (optional.isPresent()) {
            GiftCertificate found = optional.get();
            updateNotEmptyFields(certificate, found);
            found.setLastUpdateDate(getCurrentDateIso());
            GiftCertificate updated = transactionTemplate.execute(transactionStatus -> {
                GiftCertificate updating = giftCertificateDao.update(found);
                if (certificate.getTags() != null) {
                    updating.clearAllTags();
                    addTags(updating, certificate.getTags());
                }
                return updating;
            });
            optional = Optional.of(updated);
        }
        return optional;
    }

    private void updateNotEmptyFields(GiftCertificate source, GiftCertificate found) {
        if (source.getName() != null) {
            found.setName(source.getName());
        }
        if (source.getDescription() != null) {
            found.setDescription(source.getDescription());
        }
        if (source.getPrice() != null) {
            found.setPrice(source.getPrice());
        }
        if (source.getDuration() != null) {
            found.setDuration(source.getDuration());
        }
        if (source.getTags() != null) {
            found.setTags(source.getTags());
        }
    }

    @Override
    public boolean delete(long id) {
        return transactionTemplate.execute(transactionStatus -> {
            giftCertificateTagDao.deleteAllTags(id);
            return giftCertificateDao.delete(id);
        });
    }

    private String getCurrentDateIso() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
