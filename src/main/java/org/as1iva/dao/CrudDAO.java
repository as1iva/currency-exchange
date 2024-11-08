package org.as1iva.dao;

import java.util.List;

public interface CrudDAO<T> {
    T add(T t);
    List<T> getAll();
    void update(T t);

}
