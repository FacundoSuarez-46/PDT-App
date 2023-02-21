package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.AdjuntoJustificacionDAO;
import com.fannog.servidor.entities.AdjuntoJustificacion;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class AdjuntoJustificacionDAOImpl implements AdjuntoJustificacionDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public AdjuntoJustificacion create(AdjuntoJustificacion adjuntoJustificacion) throws ServicioException {
        try {
            em.persist(adjuntoJustificacion);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar adjuntar el archivo");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return adjuntoJustificacion;
    }

    @Override
    public AdjuntoJustificacion edit(AdjuntoJustificacion adjuntoJustificacion) throws ServicioException {
        try {
            em.merge(adjuntoJustificacion);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el archivo adjunto");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return adjuntoJustificacion;
    }

    @Override
    public void remove(AdjuntoJustificacion adjuntoJustificacion) throws ServicioException {
        try {
            em.remove(adjuntoJustificacion);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el archivo adjunto");
        }
    }

    @Override
    public AdjuntoJustificacion findById(Long id) {
        AdjuntoJustificacion adjuntoJustificacion = em.find(AdjuntoJustificacion.class, id);

        return adjuntoJustificacion;
    }

    @Override
    public List<AdjuntoJustificacion> findAll() {
        List<AdjuntoJustificacion> adjuntosJustificacion = em.createNamedQuery("AdjuntoJustificacion.findAll").getResultList();

        return adjuntosJustificacion;
    }

}
