package com.epam.esm.service;

import com.epam.esm.model.entity.GiftEntity;

import java.util.List;

public interface BaseService<T extends GiftEntity> {

    T findById(long id);

    List<T> findAll();

    T add(T entity);

    T update(T entity);

    void delete(long id);
}
