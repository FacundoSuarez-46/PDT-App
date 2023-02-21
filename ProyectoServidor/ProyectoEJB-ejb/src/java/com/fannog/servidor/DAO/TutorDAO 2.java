package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Tutor;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface TutorDAO extends DAO<Tutor> {

    List<Tutor> findAllWithAll();

    List<Tutor> findAllExceptOneWithAll(Long id);
}
