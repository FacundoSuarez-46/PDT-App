package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity(name = "EstadoUsuario")
@Table(name = "ESTADOS_USUARIO")
@NamedQueries({
    @NamedQuery(name = "EstadoUsuario.findAll", query = "SELECT e FROM EstadoUsuario e"),
    @NamedQuery(name = "EstadoUsuario.findByNombre", query = "SELECT e FROM EstadoUsuario e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoUsuario.findByEliminado", query = "SELECT e FROM EstadoUsuario e WHERE e.eliminado = :eliminado")})
public class EstadoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ESTADO_USUARIO_IDESTADOUSUARIO_GENERATOR", sequenceName = "SEQ_ID_ESTADO_USUARIO", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_USUARIO_IDESTADOUSUARIO_GENERATOR")
    @Column(name = "ID_ESTADO_USUARIO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "estado", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Usuario> usuarios;
}
