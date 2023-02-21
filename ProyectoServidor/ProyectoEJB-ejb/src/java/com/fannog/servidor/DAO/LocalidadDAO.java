package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Localidad;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface LocalidadDAO extends DAO<Localidad> {

    public List<Localidad> findByNombre(String nombre) throws ServicioException;
}
