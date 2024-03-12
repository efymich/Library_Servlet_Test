package org.efymich.myapp.dao;

import java.util.List;

public interface BaseDAO<T> {
    List<T> getAll();

    T getById(Long id);

    void create(T obj);

    void update(T obj);

    void delete(Long id);

}
