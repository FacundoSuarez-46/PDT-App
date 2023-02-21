package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.DAO.EstadoUsuarioDAO;
import com.fannog.servidor.entities.EstadoUsuario;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class EstadoUsuarioDAOImpl implements EstadoUsuarioDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public EstadoUsuario create(EstadoUsuario estadoUsuario) throws ServicioException {
        try {
            EstadoUsuario yaExiste = (EstadoUsuario) em.createNamedQuery("EstadoUsuario.findByNombre").setParameter("nombre", estadoUsuario.getNombre()).getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe un estado de usuario con este nombre");
            }
        } catch (NoResultException e) {
        } 

        try {
            em.persist(estadoUsuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el estado de usuario");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return estadoUsuario;
    }

    @Override
    public EstadoUsuario edit(EstadoUsuario estadoUsuario) throws ServicioException {
        try {
            em.merge(estadoUsuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el estado de usuario");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return estadoUsuario;
    }

    @Override
    public void remove(EstadoUsuario estadoUsuario) throws ServicioException {
        try {
            em.remove(estadoUsuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el estado de usuario");
        }
    }

    @Override
    public EstadoUsuario findById(Long id) {
        EstadoUsuario estadoUsuario = em.find(EstadoUsuario.class, id);

        return estadoUsuario;
    }

    @Override
    public List<EstadoUsuario> findAll() {
        List<EstadoUsuario> estadosUsuario = em.createNamedQuery("EstadoUsuario.findAll").getResultList();

        return estadosUsuario;
    }
}
