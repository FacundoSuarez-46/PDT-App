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
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "ModalidadEvento")
@Table(name = "MODALIDADES_EVENTO")
@NamedQueries({
    @NamedQuery(name = "ModalidadEvento.findAll", query = "SELECT m FROM ModalidadEvento m"),
    @NamedQuery(name = "ModalidadEvento.findByNombre", query = "SELECT m FROM ModalidadEvento m WHERE m.nombre = :nombre"),})
public class ModalidadEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "MODALIDAD_EVENTO_IDMODALIDADEVENTO_GENERATOR", sequenceName = "SEQ_ID_MODALIDAD_EVENTO", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MODALIDAD_EVENTO_IDMODALIDADEVENTO_GENERATOR")
    @Column(name = "ID_MODALIDAD_EVENTO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    @OneToMany(mappedBy = "modalidad")
    @ToString.Exclude
    private List<Evento> eventos;

    public Evento addEvento(Evento evento) {
        getEventos().add(evento);
        evento.setModalidad(this);

        return evento;
    }

    public Evento removeEvento(Evento evento) {
        getEventos().remove(evento);
        evento.setModalidad(null);

        return evento;
    }

}
