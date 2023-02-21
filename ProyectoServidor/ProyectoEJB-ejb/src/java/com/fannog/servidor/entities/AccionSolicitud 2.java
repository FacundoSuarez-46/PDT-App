package com.fannog.servidor.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "AccionSolicitud")
@Table(name = "ACCIONES_SOLICITUD")
@NamedQueries({
    @NamedQuery(name = "AccionSolicitud.findAll", query = "SELECT a FROM AccionSolicitud a"),
    @NamedQuery(name = "AccionSolicitud.findAllBySolicitud", query = "SELECT a FROM AccionSolicitud a JOIN FETCH a.solicitud s WHERE s.id = :idSolicitud")
})
public class AccionSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACCION_SOLICITUD_IDACCIONSOLICITUD_GENERATOR", sequenceName = "SEQ_ID_ACCION_SOLICITUD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCION_SOLICITUD_IDACCIONSOLICITUD_GENERATOR")
    @Column(name = "ID_ACCION_SOLICITUD", unique = true, nullable = false, precision = 38)
    @ToString.Exclude
    private Long id;

    @Column(length = 1000)
    @Size(max = 1000, message = "Superaste el limite de 1000 caracteres en el campo detalle")
    @NonNull
    private String detalle;

    @Column(name = "FEC_HORA", columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime fecHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ANALISTA", nullable = false)
    @NonNull
    @ToString.Exclude
    private Analista analista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOLICITUD")
    @NonNull
    @ToString.Exclude
    private Solicitud solicitud;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;
}
