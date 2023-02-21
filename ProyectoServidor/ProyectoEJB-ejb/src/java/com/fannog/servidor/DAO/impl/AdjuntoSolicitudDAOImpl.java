package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.DAO.AdjuntoSolicitudDAO;
import com.fannog.servidor.entities.Solicitud;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.entities.AdjuntoSolicitud;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class AdjuntoSolicitudDAOImpl implements AdjuntoSolicitudDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public AdjuntoSolicitud create(File file, Solicitud solicitud) throws ServicioException {
        try {

            byte[] bytesFromFile = null;

            try {
                bytesFromFile = Files.readAllBytes(file.toPath());
            } catch (IOException ex) {
            }

            AdjuntoSolicitud adjunto = new AdjuntoSolicitud(bytesFromFile, file.getName(), solicitud);

            em.persist(adjunto);
            em.merge(solicitud);
            em.flush();

            return adjunto;
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar adjuntar el archivo");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }
    }

    @Override
    public void remove(List<AdjuntoSolicitud> adjuntos) throws ServicioException {

        for (AdjuntoSolicitud a : adjuntos) {

            a.setEliminado(true);
            em.merge(a);
            em.flush();
        }
    }

    @Override
    public AdjuntoSolicitud findById(Long id) {
        AdjuntoSolicitud adjuntoSolicitud = em.find(AdjuntoSolicitud.class, id);

        return adjuntoSolicitud;
    }

    @Override
    public List<AdjuntoSolicitud> findAll() {
        List<AdjuntoSolicitud> adjuntosSolicitud = em.createNamedQuery("AdjuntoSolicitud.findAll").getResultList();

        return adjuntosSolicitud;
    }

    @Override
    public List<AdjuntoSolicitud> findAllBySolicitud(Long idSolicitud) {
        List<AdjuntoSolicitud> accionesSolicitud = em.createNamedQuery("AdjuntoSolicitud.findAllBySolicitud").setParameter("idSolicitud", idSolicitud).getResultList();

        return accionesSolicitud;
    }

}
