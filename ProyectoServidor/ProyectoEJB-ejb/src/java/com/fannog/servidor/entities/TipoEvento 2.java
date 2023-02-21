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
@Entity(name = "TipoEvento")
@Table(name = "TIPOS_EVENTO")
@NamedQueries({
    @NamedQuery(name = "TipoEvento.findAll", query = "SELECT t FROM TipoEvento t"),
    @NamedQuery(name = "TipoEvento.findByNombre", query = "SELECT t FROM TipoEvento t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoEvento.findByEliminado", query = "SELECT t FROM TipoEvento t WHERE t.eliminado = :eliminado")})
public class TipoEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TIPO_EVENTO_IDTIPOEVENTO_GENERATOR", sequenceName = "SEQ_ID_TIPO_EVENTO", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_EVENTO_IDTIPOEVENTO_GENERATOR")
    @Column(name = "ID_TIPO_EVENTO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "tipo")
    @ToString.Exclude
    private List<Evento> eventos;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    public Evento addEvento(Evento evento) {
        getEventos().add(evento);
        evento.setTipo(this);

        return evento;
    }

    public Evento removeEvento(Evento evento) {
        getEventos().remove(evento);
        evento.setTipo(null);

        return evento;
    }
}
