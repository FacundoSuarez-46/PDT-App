package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ACCIONES_RECLAMO")
@NamedQuery(name = "AccionReclamo.findAll", query = "SELECT a FROM AccionReclamo a")
public class AccionReclamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ACCIONES_RECLAMO_IDACCIONRECLAMO_GENERATOR", sequenceName = "SEQ_ID_ACCION_RECLAMO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACCIONES_RECLAMO_IDACCIONRECLAMO_GENERATOR")
    @Column(name = "ID_ACCION_RECLAMO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(length = 1000)
    @Size(max = 1000, message = "Superaste el limite de 1000 caracteres en el campo detalle")
    @NonNull
    private String detalle;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    @Column(name = "FEC_HORA", columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime fecHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ANALISTA", nullable = false)
    @NonNull
    @ToString.Exclude
    private Analista analista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RECLAMO", nullable = false)
    @NonNull
    @ToString.Exclude
    private Reclamo reclamo;
}
