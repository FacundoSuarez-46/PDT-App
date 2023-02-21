package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Estudiante;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface EstudianteDAO extends DAO<Estudiante> {

    public List<Estudiante> findAllWithAll();

    List<Estudiante> findAllExceptOneWithAll(Long id);
}
