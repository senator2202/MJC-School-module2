package com.epam.esm.service;

import com.epam.esm.model.entity.Tag;

import java.util.List;

/**
 * Interface provides some additional operations on Tag entity
 */
public interface TagService extends BaseService<Tag> {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Tag> findAll();
}
