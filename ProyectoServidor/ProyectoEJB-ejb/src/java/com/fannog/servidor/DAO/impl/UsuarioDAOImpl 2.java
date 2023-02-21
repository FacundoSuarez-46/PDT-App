package com.fannog.servidor.DAO.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.fannog.servidor.DAO.UsuarioDAO;
import com.fannog.servidor.entities.Usuario;
import com.fannog.servidor.exceptions.ServicioException;
import com.fannog.servidor.utils.Encryptor;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

@Stateless
public class UsuarioDAOImpl implements UsuarioDAO {

    @EJB
    private Encryptor encryptor;

    @PersistenceContext(unitName = "ProyectoEJB-ejbPU")
    private EntityManager em;

    @Override
    public Usuario create(Usuario usuario) throws ServicioException {
        try {
            Usuario yaExiste = (Usuario) em.createNamedQuery("Usuario.findByDocumento")
                    .setParameter("documento", usuario.getDocumento())
                    .getSingleResult();

            if (yaExiste != null) {
                throw new ServicioException("El usuario ya existe");
            }
        } catch (NoResultException e) {
        }

        try {
            String userName = usuario.getEmail()
                    .substring(0, usuario.getEmail().lastIndexOf("@"));
            usuario.setNombreUsuario(userName);

            String encryptedPassword = encryptor.encryptPassword(usuario.getPassword());
            usuario.setPassword(encryptedPassword);

            em.persist(usuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar crear el usuario");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return usuario;
    }

    @Override
    public Usuario edit(Usuario usuario) throws ServicioException {
        try {
            em.merge(usuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar actualizar el usuario");
        } catch (ConstraintViolationException e) {
            String errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(v -> v.getMessage().concat(",").replace(",", " "))
                    .collect(Collectors.joining("\n"));

            throw new ServicioException(errorMessages);
        }

        return usuario;
    }

    @Override
    public void remove(Usuario usuario) throws ServicioException {
        try {
            em.remove(usuario);
            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar dar de baja el usuario");
        }
    }

    @Override
    public Usuario findById(Long id) {
        Usuario usuario = em.find(Usuario.class, id);

        return usuario;
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = em.createNamedQuery("Usuario.findAll").getResultList();

        return usuarios;
    }

    @Override
    public Usuario login(String userName, String password) throws ServicioException {
        Usuario usuario = null;

        try {
            usuario = (Usuario) findByNombreUsuario(userName);

            boolean valid = encryptor.checkPassword(password, usuario.getPassword());

            if (!valid) {
                throw new ServicioException("La contraseña ingresada es incorrecta");
            }

            String estado = usuario.getEstado().getNombre();

            if (estado.equalsIgnoreCase("sin activar")) {
                throw new ServicioException("Usuario pendiente de verificación");
            }

            if (estado.equalsIgnoreCase("eliminado")) {
                throw new ServicioException("Este Usuario fue eliminado");
            }

        } catch (NoResultException e) {
            throw new ServicioException("El usuario no existe");
        }

        return usuario;
    }

    @Override
    public Usuario findByNombreUsuario(String nombreUsuario) throws ServicioException {
        Usuario usuario = (Usuario) em.
                createNamedQuery("Usuario.findByNombreUsuario").
                setParameter("nombreUsuario", nombreUsuario).
                setHint("javax.persistence.loadgraph", em.getEntityGraph("usuario.all")).
                getSingleResult();

        return usuario;
    }

    @Override
    public List<Usuario> findAllWithAll() {
        List<Usuario> usuarios = em
                .createNamedQuery("Usuario.findAll")
                .setHint("javax.persistence.loadgraph", em.getEntityGraph("usuario.all"))
                .getResultList();

        return usuarios;
    }

    @Override
    public List<Usuario> findAllExceptOneWithAll(Long id) {
        List<Usuario> usuarios = em
                .createNamedQuery("Usuario.findAllExceptOne")
                .setParameter("id", id)
                .setHint("javax.persistence.loadgraph", em.getEntityGraph("usuario.all"))
                .getResultList();

        return usuarios;
    }

    @Override
    public void changePassword(Usuario usuario, String oldPassword, String newPassword) throws ServicioException {

        try {
            if (oldPassword.equals(newPassword)) {
                throw new ServicioException("La contraseña nueva no puede coincidir con la actual");
            }

            boolean isCorrectPassword = encryptor.checkPassword(oldPassword, usuario.getPassword());

            if (!isCorrectPassword) {
                throw new ServicioException("La contraseña actual no es correcta");
            }

            String encryptedPassword = encryptor.encryptPassword(newPassword);
            usuario.setPassword(encryptedPassword);

            em.merge(usuario);

            em.flush();
        } catch (PersistenceException e) {
            throw new ServicioException("Ha ocurrido un error al intentar cambiar la contraseña");
        }

    }

}
