package com.epam.esm.service.impl;

import com.epam.esm.model.dao.GiftCertificateTagDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private TagDao dao;
    private GiftCertificateTagDao giftCertificateTagDao;

    @Autowired
    public void setDao(TagDao dao) {
        this.dao = dao;
    }

    @Autowired
    public void setGiftCertificateTagDao(GiftCertificateTagDao giftCertificateTagDao) {
        this.giftCertificateTagDao = giftCertificateTagDao;
    }

    @Override
    public Tag findById(long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    public List<Tag> findAll() {
        return dao.findAll();
    }

    @Override
    public Tag add(Tag entity) {
        return dao.add(entity);
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        Optional<Tag> optional = dao.findById(entity.getId());
        if (optional.isPresent()) {
            optional = Optional.of(dao.update(entity));
        }
        return optional;
    }

    @Override
    @Transactional
    public void delete(long id) {
        giftCertificateTagDao.deleteByTagId(id);
        dao.delete(id);
    }
}
