package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "Convocatoria")
@Table(name = "CONVOCATORIAS")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "convocatoria.all", attributeNodes = {
        @NamedAttributeNode("estudiante"),
        @NamedAttributeNode("evento"),}),})
@NamedQueries({
    @NamedQuery(name = "Convocatoria.findAll", query = "SELECT c FROM Convocatoria c"),
    @NamedQuery(name = "Convocatoria.findByEE", query = "SELECT c FROM Convocatoria c WHERE c.estudiante = :estudiante AND c.evento = :evento"),
    @NamedQuery(name = "Convocatoria.findByEvento", query = "SELECT c FROM Convocatoria c WHERE c.evento.id = :idEvento")
})
public class Convocatoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "CONVOCATORIAS_IDCONVOCATORIA_GENERATOR", sequenceName = "SEQ_ID_CONVOCATORIA", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONVOCATORIAS_IDCONVOCATORIA_GENERATOR")
    @Column(name = "ID_CONVOCATORIA", unique = true, nullable = false, precision = 38)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String asistencia;

    @Column(nullable = true)
    private String calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EVENTO", nullable = false)
    @NonNull
    @ToString.Exclude
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTUDIANTE", nullable = false)
    @NonNull
    @ToString.Exclude
    private Estudiante estudiante;

}
