package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Analista;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface AnalistaDAO extends DAO<Analista> {

    public List<Analista> findAllWithAll();
    
    List<Analista> findAllExceptOneWithAll(Long id);
}
