package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.entities.ModalidadEvento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.ModalidadEventoDAO;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class ModalidadEventoDAOImpl implements ModalidadEventoDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public ModalidadEvento create(ModalidadEvento modalidadEvento) throws ServicioException {
        try {
            ModalidadEvento yaExiste = (ModalidadEvento) em.createNamedQuery("ModalidadEvento.findByNombre").setParameter("nombre", modalidadEvento.getNombre()).getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe una modalidad de evento con este nombre");
            }
        } catch (NoResultException e) {
        }

        try {
            em.persist(modalidadEvento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear la modalidad de evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return modalidadEvento;
    }

    @Override
    public ModalidadEvento edit(ModalidadEvento modalidadEvento) throws ServicioException {
        try {
            em.merge(modalidadEvento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar la modalidad de evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return modalidadEvento;
    }

    @Override
    public void remove(ModalidadEvento modalidadEvento) throws ServicioException {
        try {
            em.remove(em.contains(modalidadEvento) ? modalidadEvento : em.merge(modalidadEvento));
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja la modalidad de evento");
        }
    }

    @Override
    public ModalidadEvento findById(Long id) {
        ModalidadEvento modalidadEvento = em
                .find(ModalidadEvento.class, id);

        return modalidadEvento;
    }

    @Override
    public List<ModalidadEvento> findAll() {
        List<ModalidadEvento> modalidades = em
                .createNamedQuery("ModalidadEvento.findAll")
                .getResultList();

        return modalidades;
    }

}
