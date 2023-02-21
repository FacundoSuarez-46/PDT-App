package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.DepartamentoDAO;
import com.fannog.servidor.entities.Departamento;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class DepartamentoDAOImpl implements DepartamentoDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Departamento create(Departamento departamento) throws ServicioException {
        try {
            Departamento yaExiste = (Departamento) em.createNamedQuery("Departamento.findByNombre").setParameter("nombre", departamento.getNombre()).getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe un departamento con este nombre");
            }
        } catch (NoResultException e) {
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        try {
            em.persist(departamento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el departamento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return departamento;
    }

    @Override
    public Departamento edit(Departamento departamento) throws ServicioException {
        try {
            em.merge(departamento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el departamento");
        }

        return departamento;
    }

    @Override
    public void remove(Departamento departamento) throws ServicioException {
        try {
            em.remove(departamento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el departamento");
        }

    }

    @Override
    public Departamento findById(Long id) {
        Departamento departamento = em.find(Departamento.class, id);

        return departamento;
    }

    @Override
    public List<Departamento> findAll() {
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findAll").getResultList();

        return departamentos;
    }

    @Override
    public List<Departamento> findAllWithLocalidades() {
        List<Departamento> departamentos = em.createNamedQuery("Departamento.findAll").setHint("javax.persistence.loadgraph",
                em.getEntityGraph("findAllWithLocalidades")).getResultList();

        return departamentos;
    }

}
