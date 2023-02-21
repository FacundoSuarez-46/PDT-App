package com.fannog.servidor.DAO;

import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface UsuarioDAO extends DAO<Usuario> {

    Usuario login(String userName, String password) throws ServicioException;

    Usuario findByNombreUsuario(String nombreUsuario) throws ServicioException;

    void changePassword(Usuario usuario, String oldPassword, String newPassword) throws ServicioException;

    List<Usuario> findAllWithAll();

    List<Usuario> findAllExceptOneWithAll(Long id);
}
