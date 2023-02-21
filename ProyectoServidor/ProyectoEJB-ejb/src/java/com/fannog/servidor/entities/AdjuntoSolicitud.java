package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "AdjuntoSolicitud")
@Table(name = "ADJUNTOS_SOLICITUD")
@NamedQueries({
    @NamedQuery(name = "AdjuntoSolicitud.findAll", query = "SELECT a FROM AdjuntoSolicitud a"),
    @NamedQuery(name = "AdjuntoSolicitud.findByNombArchivo", query = "SELECT a FROM AdjuntoSolicitud a WHERE a.nombArchivo = :nombArchivo"),
    @NamedQuery(name = "AdjuntoSolicitud.findByEliminado", query = "SELECT a FROM AdjuntoSolicitud a WHERE a.eliminado = :eliminado"),
    @NamedQuery(name = "AdjuntoSolicitud.findAllBySolicitud", query = "SELECT a FROM AdjuntoSolicitud a JOIN FETCH a.solicitud s WHERE s.id = :idSolicitud")
})
public class AdjuntoSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ADJUNTO_SOLICITUD_IDADJUNTOSOLICITUD_GENERATOR", sequenceName = "SEQ_ID_ADJUNTO_SOLICITUD", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADJUNTO_SOLICITUD_IDADJUNTOSOLICITUD_GENERATOR")
    @Column(name = "ID_ADJUNTO_SOLICITUD", unique = true, nullable = false, precision = 38)
    private Long id;

    @Lob
    @Column(nullable = false)
    @NonNull
    private byte[] archivo;

    @Column(name = "NOMB_ARCHIVO", nullable = false, length = 100)
    @NotNull(message = "El nombre del archivo no puede estar vacío")
    @Size(max = 100, message = "El nombre del archivo debe contener menos de 100 caracteres")
    @NonNull
    private String nombArchivo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_SOLICITUD", nullable = false)
    @NonNull
    private Solicitud solicitud;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    @Override
    public String toString() {
        return this.nombArchivo;
    }
}
