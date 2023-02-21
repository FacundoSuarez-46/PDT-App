package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import com.fannog.servidor.DAO.ReclamoDAO;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;

@Stateless
public class ReclamoDAOImpl implements ReclamoDAO {

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Map<String, String> countByItr() {
        Map<String, String> map = em.createNamedQuery("Reclamo.countByItr", Tuple.class)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                tuple -> (tuple.get("itr")).toString(),
                                tuple -> ((Number) tuple.get("amount")).toString()
                        )
                );

        return map;
    }

    @Override
    public Map<String, String> countByGeneracion() {
        Map<String, String> map = em.createNamedQuery("Reclamo.countByGeneracion", Tuple.class)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                tuple -> (tuple.get("generacion")).toString(),
                                tuple -> ((Number) tuple.get("amount")).toString()
                        )
                );

        return map;
    }
}
