package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Convocatoria;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface ConvocatoriaDAO extends DAO<Convocatoria> {

    public List<Convocatoria> findByEvento(Long idEvento);
}
