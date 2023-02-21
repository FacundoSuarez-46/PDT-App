package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.entities.Estudiante;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.EstudianteDAO;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class EstudianteDAOImpl implements EstudianteDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Estudiante create(Estudiante estudiante) throws ServicioException {
        try {
            em.persist(estudiante);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el estudiante");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return estudiante;
    }

    @Override
    public Estudiante edit(Estudiante estudiante) throws ServicioException {
        try {
            em.merge(estudiante);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el estudiante");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return estudiante;
    }

    @Override
    public void remove(Estudiante estudiante) throws ServicioException {
        try {
            em.remove(estudiante);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el estudiante");
        }
    }

    @Override
    public Estudiante findById(Long id) {
        Estudiante estudiante = em.find(Estudiante.class, id);

        return estudiante;
    }

    @Override
    public List<Estudiante> findAll() {
        List<Estudiante> estudiantes = em.createNamedQuery("Estudiante.findAll")
                .getResultList();

        return estudiantes;
    }

    @Override
    public List<Estudiante> findAllWithAll() {
        List<Estudiante> estudiantes = em.createNamedQuery("Estudiante.findAll")
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("usuario.all"))
                .getResultList();

        return estudiantes;
    }

    @Override
    public List<Estudiante> findAllExceptOneWithAll(Long id) {
        List<Estudiante> estudiantes = em.createNamedQuery("Estudiante.findAllExceptOne")
                .setParameter("id", id)
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("usuario.all"))
                .getResultList();

        return estudiantes;
    }
}
