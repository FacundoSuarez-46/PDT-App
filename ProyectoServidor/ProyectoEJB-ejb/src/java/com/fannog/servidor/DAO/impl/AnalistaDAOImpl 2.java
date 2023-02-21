package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.AnalistaDAO;
import com.fannog.servidor.entities.Analista;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class AnalistaDAOImpl implements AnalistaDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Analista create(Analista analista) throws ServicioException {
        try {
            em.persist(analista);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el analista");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return analista;
    }

    @Override
    public Analista edit(Analista analista) throws ServicioException {
        try {
            em.merge(analista);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el analista");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return analista;
    }

    @Override
    public void remove(Analista analista) throws ServicioException {
        try {
            em.remove(analista);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el analista");
        }
    }

    @Override
    public Analista findById(Long id) {
        Analista analista = em.find(Analista.class, id);

        return analista;
    }

    @Override
    public List<Analista> findAll() {
        List<Analista> analistas = em.createNamedQuery("Analista.findAll").getResultList();

        return analistas;
    }

    @Override
    public List<Analista> findAllWithAll() {
        List<Analista> analistas = em.createNamedQuery("Analista.findAll").setHint("javax.persistence.loadgraph",
                em.getEntityGraph("usuario.all")).getResultList();

        return analistas;
    }

     @Override
    public List<Analista> findAllExceptOneWithAll(Long id) {
        List<Analista> analistas = em.createNamedQuery("Analista.findAllExceptOne").setParameter("id", id).setHint("javax.persistence.loadgraph",
                em.getEntityGraph("usuario.all")).getResultList();

        return analistas;
    }
}
