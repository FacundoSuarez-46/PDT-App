package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Departamento;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface DepartamentoDAO extends DAO<Departamento> {
    List<Departamento> findAllWithLocalidades();
}
