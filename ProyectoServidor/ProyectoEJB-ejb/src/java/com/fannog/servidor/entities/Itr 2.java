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
@Entity(name = "Itr")
@Table(name = "ITRs")
@NamedQueries({
    @NamedQuery(name = "Itr.findAll", query = "SELECT i FROM Itr i"),
    @NamedQuery(name = "Itr.findById", query = "SELECT i FROM Itr i WHERE i.idItr = :idItr"),
    @NamedQuery(name = "Itr.findByNombre", query = "SELECT i FROM Itr i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "Itr.findByEliminado", query = "SELECT i FROM Itr i WHERE i.eliminado = :eliminado")})
public class Itr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ITR_IDITR_GENERATOR", sequenceName = "SEQ_ID_ITR", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITR_IDITR_GENERATOR")
    @Column(name = "ID_ITR", unique = true, nullable = false, precision = 38)
    private Long idItr;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "itr")
    @ToString.Exclude
    private List<Evento> eventos;

    @OneToMany(mappedBy = "itr")
    @ToString.Exclude
    private List<Usuario> usuarios;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    public Evento addEvento(Evento evento) {
        getEventos().add(evento);
        evento.setItr(this);

        return evento;
    }

    public Evento removeEvento(Evento evento) {
        getEventos().remove(evento);
        evento.setItr(null);

        return evento;
    }

}
