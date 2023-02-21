package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.entities.TipoEvento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.TipoEventoDAO;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class TipoEventoDAOImpl implements TipoEventoDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public TipoEvento create(TipoEvento tipoEvento) throws ServicioException {
        try {
            TipoEvento yaExiste = (TipoEvento) em.createNamedQuery("TipoConstancia.findByNombre").setParameter("nombre", tipoEvento.getNombre()).getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe un tipo de evento con este nombre");
            }
        } catch (NoResultException e) {
        }

        try {
            em.persist(tipoEvento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el tipo de evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return tipoEvento;
    }

    @Override
    public TipoEvento edit(TipoEvento tipoEvento) throws ServicioException {
        try {
            em.merge(tipoEvento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el tipo de evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return tipoEvento;

    }

    @Override
    public void remove(TipoEvento tipoEvento) throws ServicioException {
        try {
            em.remove(tipoEvento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el tipo de evento");
        }

    }

    @Override
    public TipoEvento findById(Long id) {
        TipoEvento tipoEvento = em.find(TipoEvento.class, id);

        return tipoEvento;
    }

    @Override
    public List<TipoEvento> findAll() {
        List<TipoEvento> tiposEvento = em.createNamedQuery("TipoEvento.findAll").getResultList();

        return tiposEvento;
    }

}
