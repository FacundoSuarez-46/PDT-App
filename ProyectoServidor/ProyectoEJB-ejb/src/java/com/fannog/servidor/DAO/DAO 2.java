package com.fannog.servidor.DAO;

import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;

public interface DAO<T> {

    T create(T entity) throws ServicioException;

    T edit(T entity) throws ServicioException;

    void remove(T entity) throws ServicioException;

    T findById(Long id);

    List<T> findAll();
}
