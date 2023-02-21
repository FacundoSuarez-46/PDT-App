package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Solicitud;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface SolicitudDAO {

    Solicitud create(Solicitud solicitud, List<File> file) throws ServicioException;

    Solicitud edit(Solicitud solicitud) throws ServicioException;

    void remove(Solicitud solicitud) throws ServicioException;

    Solicitud findById(Long id);

    List<Solicitud> findAll();

    List<Solicitud> findAllWithRelations();

    List<Solicitud> findByEstudiante(Long idEstudiante);
}
