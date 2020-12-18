package com.epam.esm.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private GiftCertificateDao dao;

    @Autowired
    public void setDao(GiftCertificateDao dao) {
        this.dao = dao;
    }

    @Override
    public GiftCertificate findById(long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return dao.findAll();
    }

    @Override
    @Transactional
    public GiftCertificate add(GiftCertificate certificate) {
        String currentDate = getCurrentDateIso();
        certificate.setCreateDate(currentDate);
        certificate.setLastUpdateDate(currentDate);
        return dao.add(certificate);
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> update(GiftCertificate certificate) {
        Optional<GiftCertificate> optional = dao.findById(certificate.getId());
        if (optional.isPresent()) {
            GiftCertificate found = optional.get();
            copyEmptyFields(found, certificate);
            certificate.setLastUpdateDate(getCurrentDateIso());
            optional = Optional.of(dao.update(certificate));
        }
        return optional;
    }

    private void copyEmptyFields(GiftCertificate found, GiftCertificate modifiable) {
        if (modifiable.getName() == null) {
            modifiable.setName(found.getName());
        }
        if (modifiable.getDescription() == null) {
            modifiable.setDescription(found.getDescription());
        }
        if (modifiable.getPrice() == null) {
            modifiable.setPrice(found.getPrice());
        }
        if (modifiable.getDuration() == null) {
            modifiable.setDuration(found.getDuration());
        }
        if (modifiable.getTags() == null) {
            modifiable.setTags(found.getTags());
        }
    }

    @Override
    public void delete(long id) {
        dao.delete(id);
    }

    private String getCurrentDateIso() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }
}
