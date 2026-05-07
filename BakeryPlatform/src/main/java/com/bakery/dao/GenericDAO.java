package com.bakery.dao;

import java.util.List;

/**
 * GenericDAO — OOP: ABSTRACTION (interface contract)
 * All DAOs implement this interface — enables Polymorphism.
 * T = entity type, ID = identifier type
 */
public interface GenericDAO<T, ID> {
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
