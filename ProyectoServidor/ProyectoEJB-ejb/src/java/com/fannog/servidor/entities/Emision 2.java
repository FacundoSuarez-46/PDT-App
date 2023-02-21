package com.fannog.servidor.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "Emision")
@Table(name = "EMISIONES")
@NamedQueries({
    @NamedQuery(name = "Emision.findAll", query = "SELECT e FROM Emision e"),
    @NamedQuery(name = "Emision.findByFecha", query = "SELECT e FROM Emision e WHERE e.fecha = :fecha")})
public class Emision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_SOLICITUD", nullable = false)
    private Long id;

    @Lob
    @Column(nullable = false)
    @NonNull
    private byte[] archivo;

    @Column(name = "FEC_HORA", columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ANALISTA", nullable = false)
    @NonNull
    private Analista analista;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "ID_SOLICITUD", nullable = false)
    @NonNull
    private Solicitud solicitud;

}
