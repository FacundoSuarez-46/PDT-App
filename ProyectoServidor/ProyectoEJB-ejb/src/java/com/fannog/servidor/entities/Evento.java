package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "Evento")
@Table(name = "EVENTOS")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "evento.all", attributeNodes = {
        @NamedAttributeNode("estado"),
        @NamedAttributeNode("itr"),
        @NamedAttributeNode("modalidad"),
        @NamedAttributeNode("tipo"),
        @NamedAttributeNode("convocatorias"),}),})
@NamedQueries({
    @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
    @NamedQuery(name = "Evento.findByFecHoraInicio", query = "SELECT e FROM Evento e WHERE e.fecHoraInicio = :fecHoraInicio"),
    @NamedQuery(name = "Evento.findByNombre", query = "SELECT e FROM Evento e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Evento.findByFecHoraFinal", query = "SELECT e FROM Evento e WHERE e.fecHoraFinal = :fecHoraFinal"),
    @NamedQuery(name = "Evento.findBetweenDates", query = "SELECT e FROM Evento e WHERE e.fecHoraInicio >= :fecHoraInicio AND e.fecHoraFinal <= :fecHoraFinal"),})
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "EVENTO_IDEVENTO_GENERATOR", sequenceName = "SEQ_ID_EVENTO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENTO_IDEVENTO_GENERATOR")
    @Column(name = "ID_EVENTO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, length = 50)
    @Size(max = 50, min = 2, message = "El campo nombre debe contener entre 2 a 50 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @Column(nullable = true, length = 50)
    @Size(max = 50, message = "El campo localización debe contener máximo 50 caracteres")
    @NonNull
    private String localizacion;

    @OneToMany(mappedBy = "evento")
    @ToString.Exclude
    private List<Justificacion> justificaciones;

    @OneToMany(mappedBy = "evento")
    @ToString.Exclude
    private List<Convocatoria> convocatorias;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "RESPONSABILIDAD",
            joinColumns = {
                @JoinColumn(name = "ID_EVENTO", nullable = false)
            },
            inverseJoinColumns = {
                @JoinColumn(name = "ID_TUTOR", nullable = false)
            }
    )
    @ToString.Exclude
    @Column
    private List<Tutor> tutores;

    @Column(name = "FEC_HORA_FINAL", nullable = false)
    @Future(message = "La fecha hora final no puede ser menor a la actual")
    @NonNull
    @Getter(AccessLevel.NONE)
    private LocalDateTime fecHoraFinal;

    @Column(name = "FEC_HORA_INICIO", nullable = false)
    @Future(message = "La fecha hora de inicio no puede ser menor a la actual")
    @NonNull
    @Getter(AccessLevel.NONE)
    private LocalDateTime fecHoraInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    @NotNull(message = "Debes seleccionar un estado de evento")
    @NonNull
    @ToString.Exclude
    private EstadoEvento estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ITR", nullable = false)
    @ToString.Exclude
    @NonNull
    private Itr itr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODALIDAD", nullable = false)
    @NotNull(message = "Debes seleccionar una modalidad de evento")
    @NonNull
    @ToString.Exclude
    private ModalidadEvento modalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO", nullable = false)
    @NotNull(message = "Debes seleccionar un tipo de evento")
    @NonNull
    @ToString.Exclude
    private TipoEvento tipo;

    @OneToMany(mappedBy = "evento")
    @ToString.Exclude
    private List<Solicitud> solicitudes;

    public void addTutor(Tutor tutor) {
        if (this.tutores == null) {
            this.tutores = new ArrayList<>();
        }

        tutores.add(tutor);
        tutor.getEventos().add(this);
    }

    public void removeTutor(Tutor tutor) {
        if (this.tutores != null) {
            tutores.remove(tutor);
            tutor.getEventos().remove(this);
        }
    }

    public String getFecHoraFinal() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm");

        return fecHoraFinal.format(dateTimeFormatter);
    }

    public String getFecHoraInicio() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm");

        return fecHoraInicio.format(dateTimeFormatter);
    }

}
