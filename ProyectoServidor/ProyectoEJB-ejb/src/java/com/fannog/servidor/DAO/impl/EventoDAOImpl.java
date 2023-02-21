package com.fannog.servidor.DAO.impl;

import com.fannog.servidor.entities.Evento;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.EventoDAO;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.exceptions.ServicioException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class EventoDAOImpl implements EventoDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Evento create(Evento evento, List<Tutor> tutores) throws ServicioException {
        try {
            em.persist(evento);

            tutores.forEach(t -> {
                evento.addTutor(t);
            });

            em.merge(evento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return evento;
    }

    @Override
    public Evento edit(Evento evento, List<Tutor> tutores) throws ServicioException {
        try {
            List<Tutor> misTutores = new ArrayList<>();
            misTutores.addAll(evento.getTutores());

            //Si un tutor no estaba lo agrega
            for (Tutor t : tutores) {
                if (!misTutores.contains(t)) {
                    evento.addTutor(t);
                }
            }

            //Si un tutor estaba y ahora no esta, lo remueve
            for (Tutor t : misTutores) {
                if (!tutores.contains(t)) {
                    evento.removeTutor(t);
                }
            }

            em.merge(evento);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el evento");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return evento;
    }

    @Override
    public void remove(Evento evento) throws ServicioException {
        try {
            em.remove(em.contains(evento) ? evento : em.merge(evento));
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el evento");
        }
    }

    @Override
    public Evento findById(Long id) {
        Evento evento = em.find(Evento.class, id);

        return evento;
    }

    @Override
    public List<Evento> findAll() {
        List<Evento> eventos = em.createNamedQuery("Evento.findAll")
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("evento.all"))
                .getResultList();

        return eventos;
    }

    @Override
    public List<Evento> findBetweenDates(LocalDateTime inicio, LocalDateTime fin) {
        List<Evento> eventos = em.createNamedQuery("Evento.findBetweenDates")
                .setParameter("fecHoraInicio", inicio)
                .setParameter("fecHoraFinal", fin)
                .setHint("javax.persistence.loadgraph",
                        em.getEntityGraph("evento.all"))
                .getResultList();

        return eventos;
    }

}
