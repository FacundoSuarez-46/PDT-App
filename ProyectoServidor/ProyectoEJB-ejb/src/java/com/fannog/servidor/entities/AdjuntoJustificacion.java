package com.fannog.servidor.entities;

import java.io.File;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "AdjuntoJustificacion")
@Table(name = "ADJUNTOS_JUSTIFICACION")
@NamedQueries({
    @NamedQuery(name = "AdjuntoJustificacion.findAll", query = "SELECT a FROM AdjuntoJustificacion a"),
    @NamedQuery(name = "AdjuntoJustificacion.findByNombArchivo", query = "SELECT a FROM AdjuntoJustificacion a WHERE a.nombArchivo = :nombArchivo"),
    @NamedQuery(name = "AdjuntoJustificacion.findByEliminado", query = "SELECT a FROM AdjuntoJustificacion a WHERE a.eliminado = :eliminado")})
public class AdjuntoJustificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ADJUNTO_JUSTIFICACION_IDADJUNTOJUSTIFICACION_GENERATOR", sequenceName = "SEQ_ID_ADJUNTOS_JUSTIFICACION", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADJUNTO_JUSTIFICACION_IDADJUNTOJUSTIFICACION_GENERATOR")
    @Column(name = "ID_ADJUNTO_JUSTIFICACION", unique = true, nullable = false, precision = 38)
    private Long id;

    @Lob
    @Column(nullable = false)
    @NotNull(message = "Debes adjuntar al menos un archivo")
    @NonNull()
    private File archivo;

    @Column(name = "NOMB_ARCHIVO", nullable = false, length = 100)
    @NotNull(message = "El nombre del archivo no puede estar vacío")
    @Max(value = 100, message = "El nombre del archivo debe contener menos de 100 caracteres")
    @NonNull
    private String nombArchivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_JUSTIFICACION", nullable = true)
    @NonNull
    private Justificacion justificacion;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

}
