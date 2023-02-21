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
@Entity(name = "EstadoSolicitud")
@Table(name = "ESTADOS_SOLICITUD")
@NamedQueries({
    @NamedQuery(name = "EstadoSolicitud.findAll", query = "SELECT e FROM EstadoSolicitud e"),
    @NamedQuery(name = "EstadoSolicitud.findByNombre", query = "SELECT e FROM EstadoSolicitud e WHERE UPPER(e.nombre) = UPPER(:nombre)"),
    @NamedQuery(name = "EstadoSolicitud.findByEliminado", query = "SELECT e FROM EstadoSolicitud e WHERE e.eliminado = :eliminado")})
public class EstadoSolicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "ESTADOS_SOLICITUD_IDESTADOSOLICITUD_GENERATOR", sequenceName = "SEQ_ID_ESTADO_SOLICITUD", allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADOS_SOLICITUD_IDESTADOSOLICITUD_GENERATOR")
    @Column(name = "ID_ESTADO_SOLICITUD", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false, length = 25)
    @Size(max = 25, min = 2, message = "El campo nombre debe contener entre 2 a 25 caracteres")
    @NotNull(message = "El campo nombre no puede estar vacío")
    @NonNull
    private String nombre;

    @OneToMany(mappedBy = "estado")
    @ToString.Exclude
    private List<Solicitud> solicitudes;

    @Column(nullable = false, precision = 1, columnDefinition = "NUMBER(1, 0) DEFAULT 0")
    private boolean eliminado;
}
