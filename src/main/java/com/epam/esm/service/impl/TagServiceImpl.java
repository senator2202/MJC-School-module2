package com.epam.esm.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private TagDao dao;

    @Autowired
    public void setDao(TagDao dao) {
        this.dao = dao;
    }

    @Override
    public Tag findById(long id) {
        return dao.findById(id);
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
    public Tag update(Tag entity) {
        return dao.update(entity);
    }

    @Override
    public void delete(long id) {
        dao.delete(id);
    }
}
