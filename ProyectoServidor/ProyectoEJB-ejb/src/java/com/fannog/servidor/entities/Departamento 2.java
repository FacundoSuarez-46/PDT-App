package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "Departamento")
@Table(name = "DEPARTAMENTOS")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "findAllWithLocalidades", attributeNodes = {
        @NamedAttributeNode("localidades")
    })})
@NamedQueries({
    @NamedQuery(name = "Departamento.findAll", query = "SELECT d FROM Departamento d"),
    @NamedQuery(name = "Departamento.findByNombre", query = "SELECT d FROM Departamento d WHERE d.nombre = :nombre"),})
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "DEPARTAMENTO_IDDEPARTAMENTO_GENERATOR", sequenceName = "SEQ_ID_DEPARTAMENTO", allocationSize = 1, initialValue = 20)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTAMENTO_IDDEPARTAMENTO_GENERATOR")
    @Column(name = "ID_DEPARTAMENTO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(length = 25, nullable = false)
    @Size(min = 2, max = 25, message = "El nombre debe contener entre 2 a 25 caracteres")
    @Pattern(regexp = "^[A-Za-z]*$", message = "El nombre no puede contener números")
    @NotNull
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "departamento")
    @ToString.Exclude
    private List<Localidad> localidades;

    public Localidad addLocalidad(Localidad localidad) {
        getLocalidades().add(localidad);
        localidad.setDepartamento(this);

        return localidad;
    }

    public Localidad removeLocalidad(Localidad localidad) {
        getLocalidades().remove(localidad);
        localidad.setDepartamento(null);

        return localidad;
    }

}
