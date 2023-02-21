package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.EstadoSolicitud;
import javax.ejb.Remote;

@Remote
public interface EstadoSolicitudDAO extends DAO<EstadoSolicitud> {

    EstadoSolicitud findByNombre(String nombre);
}
