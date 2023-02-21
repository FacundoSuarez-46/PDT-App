package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name = "Tutor")
@Table(name = "TUTORES")
@NamedQueries({
    @NamedQuery(name = "Tutor.findAll", query = "SELECT t FROM Tutor t"),
    @NamedQuery(name = "Tutor.findAllExceptOne", query = "SELECT t FROM Tutor t WHERE NOT t.id = :id"),
    @NamedQuery(name = "Tutor.findByArea", query = "SELECT t FROM Tutor t WHERE t.area = :area"),
    @NamedQuery(name = "Tutor.findByRol", query = "SELECT t FROM Tutor t WHERE t.rol = :rol")})
public class Tutor extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, length = 50)
    @Size(max = 50, min = 2, message = "El campo area debe contener entre 2 a 50 caracteres")
    @NotNull(message = "El campo area no puede estar vacío")
    private String area;

    @Column(nullable = false, length = 50)
    @Size(max = 50, min = 2, message = "El campo rol debe contener entre 2 a 50 caracteres")
    @NotNull(message = "El campo rol no puede estar vacío")
    private String rol;

    @ManyToMany(mappedBy = "tutores", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Evento> eventos;

    public Tutor(String area, String rol, String apellidos, String documento, String email, String nombres, Integer telefono, String password, EstadoUsuario estado, Localidad localidad, Itr itr) {
        super(apellidos, documento, email, nombres, telefono, password, estado, localidad, itr);
        this.area = area;
        this.rol = rol;
    }

}
