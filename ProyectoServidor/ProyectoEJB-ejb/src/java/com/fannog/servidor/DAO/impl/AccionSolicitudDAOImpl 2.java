package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.AccionSolicitudDAO;
import com.fannog.servidor.entities.AccionSolicitud;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class AccionSolicitudDAOImpl implements AccionSolicitudDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public AccionSolicitud create(AccionSolicitud accionSolicitud) throws ServicioException {
        try {
            em.persist(accionSolicitud);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar registrar la accion");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return accionSolicitud;
    }

    @Override
    public AccionSolicitud edit(AccionSolicitud accionSolicitud) throws ServicioException {
        try {
            em.merge(accionSolicitud);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar la acción");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return accionSolicitud;
    }

    @Override
    public void remove(AccionSolicitud accionSolicitud) throws ServicioException {
        try {
            em.remove(accionSolicitud);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja la acción");
        }
    }

    @Override
    public AccionSolicitud findById(Long id) {
        AccionSolicitud accionSolicitud = em.find(AccionSolicitud.class, id);

        return accionSolicitud;
    }

    @Override
    public List<AccionSolicitud> findAll() {
        List<AccionSolicitud> accionesSolicitud = em.createNamedQuery("AccionSolicitud.findAll").getResultList();

        return accionesSolicitud;
    }

    @Override
    public List<AccionSolicitud> findAllBySolicitud(Long idSolicitud) {
        List<AccionSolicitud> accionesSolicitud = em.createNamedQuery("AccionSolicitud.findAllBySolicitud").setParameter("idSolicitud", idSolicitud).getResultList();

        return accionesSolicitud;
    }

}
