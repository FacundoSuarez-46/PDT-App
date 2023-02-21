package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Evento;
import com.fannog.servidor.entities.Tutor;
import com.fannog.servidor.exceptions.ServicioException;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface EventoDAO {

    Evento create(Evento evento, List<Tutor> tutores) throws ServicioException;

    Evento edit(Evento evento, List<Tutor> tutores) throws ServicioException;

    void remove(Evento evento) throws ServicioException;

    Evento findById(Long id);

    List<Evento> findAll();

    List<Evento> findBetweenDates(LocalDateTime inicio, LocalDateTime fin);
}
