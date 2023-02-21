package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "RECLAMOS")
@NamedQueries({
    @NamedQuery(name = "Reclamo.findAll", query = "SELECT r FROM Reclamo r"),
    @NamedQuery(name = "Reclamo.countByItr", query = "SELECT i.nombre as itr, COUNT(r.id) AS amount FROM Reclamo r JOIN r.estudiante e RIGHT JOIN e.itr i GROUP BY i.nombre ORDER BY amount DESC"),
     @NamedQuery(name = "Reclamo.countByGeneracion", query = "SELECT e.generacion as generacion, COUNT(r.id) AS amount FROM Reclamo r RIGHT JOIN r.estudiante e GROUP BY e.generacion ORDER BY amount DESC")
})
public class Reclamo implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @SequenceGenerator(name = "RECLAMOS_IDRECLAMO_GENERATOR", sequenceName = "SEQ_ID_RECLAMOS", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECLAMOS_IDRECLAMO_GENERATOR")
    @Column(name = "ID_RECLAMO", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    @Max(1000)
    @NonNull
    private int creditos;

    @Column(nullable = false, length = 1000)
    @Size(min = 2, max = 1000, message = "El campo descripcion debe contener entre 2 a 50 caracteres")
    private String descripcion;

    @Column(name = "FEC_HORA", columnDefinition = "TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime fecHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    @NonNull
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EVENTO", nullable = false)
    @NonNull
    private Evento evento;

    @Column(nullable = false)
    @Min(1)
    @Max(20)
    @NonNull
    private int semestre;

    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "El campo nombre debe contener entre 2 a 50 caracteres")
    @NonNull
    private String titulo;

    @OneToMany(mappedBy = "reclamo")
    private List<AccionReclamo> acciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO", nullable = false)
    private EstadoReclamo estado;
}
