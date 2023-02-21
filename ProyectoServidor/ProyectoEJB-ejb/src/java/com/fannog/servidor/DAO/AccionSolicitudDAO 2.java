package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.AccionSolicitud;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AccionSolicitudDAO extends DAO<AccionSolicitud> {

    List<AccionSolicitud> findAllBySolicitud(Long idSolicitud);
}
