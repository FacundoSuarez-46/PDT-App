package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.TipoConstancia;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface TipoConstanciaDAO {

    TipoConstancia create(TipoConstancia tipo, File file) throws ServicioException;

    TipoConstancia edit(TipoConstancia tipo, File file) throws ServicioException;

    void remove(TipoConstancia tipo) throws ServicioException;

    TipoConstancia findById(Long id);

    List<TipoConstancia> findAll();
}
