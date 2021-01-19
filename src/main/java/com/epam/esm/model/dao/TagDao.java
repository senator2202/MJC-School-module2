package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface provides additional operations with DB for Tag entity.
 */
public interface TagDao extends BaseDao<Tag> {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Tag> findAll();

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Tag> findByName(String name);
}
