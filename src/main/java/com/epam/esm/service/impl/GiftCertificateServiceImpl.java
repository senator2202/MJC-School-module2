package com.epam.esm.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        return dao.findById(id);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return dao.findAll();
    }

    @Override
    public GiftCertificate add(GiftCertificate certificate) {
        String currentDate = getCurrentDateIso();
        certificate.setCreateDate(currentDate);
        certificate.setLastUpdateDate(currentDate);
        return dao.add(certificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate certificate) {
        certificate.setLastUpdateDate(getCurrentDateIso());
        return dao.update(certificate);
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
