package com.fannog.servidor.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data()
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name = "Analista")
@Table(name = "ANALISTAS")
@NamedQueries({
    @NamedQuery(name = "Analista.findAll", query = "SELECT a FROM Analista a"),
    @NamedQuery(name = "Analista.findAllExceptOne", query = "SELECT a FROM Analista a WHERE NOT a.id = :id"),})
public class Analista extends Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "analista")
    private List<AccionJustificacion> accionesJustificacion;

    @OneToMany(mappedBy = "analista")
    private List<AccionSolicitud> accionesSolicitud;

    @OneToMany(mappedBy = "analista")
    @ToString.Exclude
    private List<AccionReclamo> accionesReclamo;

    public Analista(String apellidos, String documento, String email, String nombres, Integer telefono, String password, EstadoUsuario estado, Localidad localidad, Itr itr) {
        super(apellidos, documento, email, nombres, telefono, password, estado, localidad, itr);
    }
}
