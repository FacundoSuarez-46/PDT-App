package com.fannog.servidor.DAO;

import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface ReclamoDAO {

    Map<String, String> countByItr();

    Map<String, String> countByGeneracion();
}
