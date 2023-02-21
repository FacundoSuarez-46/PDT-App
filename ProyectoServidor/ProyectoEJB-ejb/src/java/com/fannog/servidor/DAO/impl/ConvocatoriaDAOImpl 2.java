package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import com.fannog.servidor.DAO.ConvocatoriaDAO;
import com.fannog.servidor.entities.Convocatoria;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class ConvocatoriaDAOImpl implements ConvocatoriaDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Convocatoria create(Convocatoria convocatoria) throws ServicioException {
        try {
            Convocatoria yaExiste = (Convocatoria) em.createNamedQuery("Convocatoria.findByEE")
                    .setParameter("estudiante", convocatoria.getEstudiante())
                    .setParameter("evento", convocatoria.getEvento())
                    .setHint("javax.persistence.loadgraph",
                            em.getEntityGraph("convocatoria.all"))
                    .getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("Ya existe una convoctoria de este estudiante a este evento");
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
            em.persist(convocatoria);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear la convocatoria");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return convocatoria;
    }

    @Override
    public Convocatoria edit(Convocatoria convocatoria) throws ServicioException {
        try {
            em.merge(convocatoria);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar la convocatoria");
        }

        return convocatoria;
    }

    @Override
    public void remove(Convocatoria convocatoria) throws ServicioException {
        try {
            em.remove(em.contains(convocatoria) ? convocatoria : em.merge(convocatoria));
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja la convocatoria");
        }
    }

    @Override
    public Convocatoria findById(Long id) {
        Convocatoria convocatoria = em.find(Convocatoria.class, id);

        return convocatoria;
    }

    @Override
    public List<Convocatoria> findAll() {
        List<Convocatoria> convocatorias = em.createNamedQuery("Convocatoria.findAll")
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("convocatoria.all"))
                .getResultList();

        return convocatorias;
    }

    @Override
    public List<Convocatoria> findByEvento(Long idEvento) {
        List<Convocatoria> convocatorias = em.createNamedQuery("Convocatoria.findByEvento")
                .setParameter("idEvento", idEvento)
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("convocatoria.all"))
                .getResultList();

        return convocatorias;
    }
}
