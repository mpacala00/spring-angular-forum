package com.github.mpacala00.forum.service.data;

import java.util.Set;

/**
 * <p>basic interface for services performing CRUD operations</p>
 * @param <T> entity class for concrete service
 * @param <ID> type of field annotated as ID in specified entity
 */
public interface CrudService <T, ID> {

    Set<T> findAll();

    T findById(ID id);

    T save(T entity);

    void delete(T entity);

    void deleteById(ID id);
}
