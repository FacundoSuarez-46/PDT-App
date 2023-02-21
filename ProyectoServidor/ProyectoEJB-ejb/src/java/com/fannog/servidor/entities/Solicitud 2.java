package com.fannog.servidor.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.persistence.*;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "Solicitud")
@Table(name = "SOLICITUDES")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "solicitud.all", attributeNodes = {
        @NamedAttributeNode("estado"),
        @NamedAttributeNode("estudiante"),
        @NamedAttributeNode("evento"),
        @NamedAttributeNode("tipo"),
        @NamedAttributeNode("estado"),}),})
@NamedQueries({
    @NamedQuery(name = "Solicitud.findAll", query = "SELECT s FROM Solicitud s ORDER BY s.fecha"),
    @NamedQuery(name = "Solicitud.findByDetalle", query = "SELECT s FROM Solicitud s WHERE s.detalle = :detalle"),
    @NamedQuery(name = "Solicitud.findByEstudiante", query = "SELECT s FROM Solicitud s WHERE s.estudiante.id = :idEstudiante ORDER BY s.fecha"),})
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SOLICITUD_IDSOLICITUD_GENERATOR", sequenceName = "SEQ_ID_SOLICITUD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLICITUD_IDSOLICITUD_GENERATOR")
    @Column(name = "ID_SOLICITUD", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = true, length = 1000)
    @Size(max = 1000, message = "Superaste el limite de 1000 caracteres en el campo detalle")
    @NonNull
    private String detalle;

    @Column(name = "FEC_HORA", columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime fecha;

    @OneToMany(mappedBy = "solicitud")
    @ToString.Exclude
    private List<AccionSolicitud> acciones;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<AdjuntoSolicitud> adjuntos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    @NonNull
    private EstadoSolicitud estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    @NonNull
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CONSTANCIA", nullable = false)
    @NonNull
    private TipoConstancia tipo;

    @OneToOne(mappedBy = "solicitud", optional = true, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn()
    private Emision emision;

    @ManyToOne
    @JoinColumn(name = "ID_EVENTO", nullable = false)
    @NonNull
    private Evento evento;

    public AccionSolicitud addAccion(AccionSolicitud accion) {
        getAcciones().add(accion);
        accion.setSolicitud(this);

        return accion;
    }

    public AccionSolicitud removeAccion(AccionSolicitud accion) {
        getAcciones().remove(accion);
        accion.setSolicitud(null);

        return accion;
    }

    public AdjuntoSolicitud addAdjunto(AdjuntoSolicitud adjunto) {
        getAdjuntos().add(adjunto);
        adjunto.setSolicitud(this);

        return adjunto;
    }

    public AdjuntoSolicitud removeAdjunto(AdjuntoSolicitud adjunto) {
        getAdjuntos().remove(adjunto);
        adjunto.setSolicitud(null);

        return adjunto;
    }
}
