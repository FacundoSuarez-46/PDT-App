package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.entities.TipoConstancia;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class TipoConstanciaDAOImpl implements TipoConstanciaDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public TipoConstancia create(TipoConstancia tipo, File file) throws ServicioException {
        try {
            TipoConstancia yaExiste = (TipoConstancia) em.createNamedQuery("TipoConstancia.findByNombre")
                    .setParameter("nombre", tipo.getNombre())
                    .getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe un tipo de constancia con este nombre");
            }
        } catch (NoResultException e) {
        }

        try {
            byte[] plantilla = Files.readAllBytes(file.toPath());
            tipo.setPlantilla(plantilla);

            em.persist(tipo);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el tipo de constancia");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v
                            -> v.getMessage()
                            .concat(",")
                            .replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        } catch (IOException ex) {
            throw new ServicioException("Ha ocurrido un error al intentar guardar la plantilla");
        }

        return tipo;
    }

    @Override
    public TipoConstancia edit(TipoConstancia tipo, File file) throws ServicioException {
        try {
            if (file != null) {
                byte[] plantilla = Files.readAllBytes(file.toPath());
                tipo.setPlantilla(plantilla);
            }

            em.merge(em.contains(tipo) ? tipo : em.merge(tipo));
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el tipo de constancia");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        } catch (IOException ex) {
            throw new ServicioException("Ha ocurrido un error al intentar guardar la plantilla");
        }

        return tipo;
    }

    @Override
    public void remove(TipoConstancia tipo) throws ServicioException {
        try {
            em.remove(tipo);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el tipo de constancia");
        }
    }

    @Override
    public TipoConstancia findById(Long id) {
        TipoConstancia tipo = em
                .find(TipoConstancia.class, id);

        return tipo;
    }

    @Override
    public List<TipoConstancia> findAll() {
        List<TipoConstancia> tipos = em
                .createNamedQuery("TipoConstancia.findAll")
                .getResultList();

        return tipos;

    }

}
