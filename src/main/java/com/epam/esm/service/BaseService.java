package com.epam.esm.service;

import com.epam.esm.model.entity.GiftEntity;

import java.util.Optional;

/**
 * Interface provides service for CRUD operations for project entities.
 *
 * @param <T> the type parameter
 */
public interface BaseService<T extends GiftEntity> {

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<T> findById(long id);

    /**
     * Add T entity.
     *
     * @param entity the entity
     * @return the t
     */
    T add(T entity);

    /**
     * Update optional. Entity is wrapped by optional because it could not be found in database
     *
     * @param entity the entity
     * @return the optional
     */
    Optional<T> update(T entity);

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(long id);
}
