package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.AdjuntoSolicitud;
import com.fannog.servidor.entities.Solicitud;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AdjuntoSolicitudDAO {

    AdjuntoSolicitud create(File file, Solicitud solicitud) throws ServicioException;

    void remove(List<AdjuntoSolicitud> adjuntos) throws ServicioException;

    AdjuntoSolicitud findById(Long id) throws ServicioException;

    List<AdjuntoSolicitud> findAll() throws ServicioException;

    List<AdjuntoSolicitud> findAllBySolicitud(Long idSolicitud) throws ServicioException;

}
