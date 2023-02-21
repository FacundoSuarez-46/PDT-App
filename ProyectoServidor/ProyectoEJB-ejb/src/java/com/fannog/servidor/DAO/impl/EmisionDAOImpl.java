package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.entities.Emision;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import com.fannog.servidor.DAO.EmisionDAO;

@Stateless
public class EmisionDAOImpl implements EmisionDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Emision create(Emision emision) throws ServicioException {
        try {
            em.persist(emision);
            em.flush();
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new ServicioException("Ha ocurrido un error al intentar emitir la solicitud de constancia");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return emision;
    }

    @Override
    public Emision edit(Emision emision) throws ServicioException {
        try {
            em.merge(emision);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar la constancia");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return emision;
    }

    @Override
    public void remove(Emision emision) throws ServicioException {
        try {
            em.remove(emision);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja la constancia");
        }
    }

    @Override
    public Emision findById(Long id) {
        Emision emision = em.find(Emision.class, id);

        return emision;
    }

    @Override
    public List<Emision> findAll() {
        List<Emision> emisiones = em.createNamedQuery("Emision.findAll").getResultList();

        return emisiones;
    }

}
