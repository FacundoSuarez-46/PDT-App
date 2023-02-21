package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.NonNull;
import lombok.ToString;

@Entity
@Table(name = "ESTADOS_RECLAMO")
@NamedQuery(name = "EstadoReclamo.findAll", query = "SELECT e FROM EstadoReclamo e")
public class EstadoReclamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ESTADOS_RECLAMO_IDESTADOSRECLAMO_GENERATOR", sequenceName = "SEQ_ID_ESTADOS_RECLAMO", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADOS_RECLAMO_IDESTADOSRECLAMO_GENERATOR")
    @Column(name = "ID_ESTADO_RECLAMO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "estado")
    @ToString.Exclude
    private List<Reclamo> reclamos;

}
