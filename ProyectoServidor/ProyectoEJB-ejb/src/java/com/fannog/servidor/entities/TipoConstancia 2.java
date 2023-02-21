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
@Entity(name = "TipoConstancia")
@Table(name = "TIPOS_CONSTANCIA")
@NamedQueries({
    @NamedQuery(name = "TipoConstancia.findAll", query = "SELECT t FROM TipoConstancia t"),
    @NamedQuery(name = "TipoConstancia.findByNombre", query = "SELECT t FROM TipoConstancia t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoConstancia.findByEliminado", query = "SELECT t FROM TipoConstancia t WHERE t.eliminado = :eliminado")})
public class TipoConstancia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "TIPO_CONSTANCIA_IDTIPOCONSTANCIA_GENERATOR", sequenceName = "SEQ_ID_TIPO_CONSTANCIA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_CONSTANCIA_IDTIPOCONSTANCIA_GENERATOR")
    @Column(name = "ID_TIPO_CONSTANCIA", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @Lob
    @Column(nullable = false)
    @NotNull(message = "Debes adjuntar una plantilla")
    private byte[] plantilla;

    @OneToMany(mappedBy = "tipo")
    @ToString.Exclude
    private List<Solicitud> solicitudes;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;

    public Solicitud addSolicitud(Solicitud solicitud) {
        getSolicitudes().add(solicitud);
        solicitud.setTipo(this);

        return solicitud;
    }

    public Solicitud removeSolicitud(Solicitud solicitud) {
        getSolicitudes().remove(solicitud);
        solicitud.setTipo(null);

        return solicitud;
    }

}
