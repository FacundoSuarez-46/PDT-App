package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data()
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "Estudiante")
@Table(name = "ESTUDIANTES")
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findAllExceptOne", query = "SELECT e FROM Estudiante e WHERE NOT e.id = :id"),
    @NamedQuery(name = "Estudiante.findByGeneracion", query = "SELECT e FROM Estudiante e WHERE e.generacion = :generacion")})
public class Estudiante extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false, precision = 4)
    @Positive(message = "El campo generacion debe contener valores positivos")
    private Integer generacion;

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    private List<Solicitud> solicitudes;

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    private List<Reclamo> reclamos;

    @OneToMany(mappedBy = "estudiante")
    @ToString.Exclude
    private List<Convocatoria> convocatorias;

    public Estudiante(Integer generacion, String apellidos, String documento, String email, String nombres, Integer telefono, String password, EstadoUsuario estado, Localidad localidad, Itr itr) {
        super(apellidos, documento, email, nombres, telefono, password, estado, localidad, itr);
        this.generacion = generacion;
    }

}
