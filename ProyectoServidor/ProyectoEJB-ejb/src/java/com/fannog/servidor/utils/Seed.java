package com.fannog.servidor.utils;

import com.fannog.servidor.DAO.TipoConstanciaDAO;
import com.fannog.servidor.entities.Analista;
import com.fannog.servidor.entities.TipoConstancia;
import com.fannog.servidor.exceptions.ServicioException;
import java.io.File;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

@Singleton
@LocalBean
@Startup
public class Seed {

    @EJB
    private TipoConstanciaDAO tipoConstanciaDAO;

    @PostConstruct
    public void init() {
        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "/META-INF/defaultTemplates/";

            File plantillaTransporte = new File(path + "Plantilla_Transporte.doc");

            TipoConstancia transporte = new TipoConstancia("TRANSPORTE");

            tipoConstanciaDAO.create(transporte, plantillaTransporte);

            File plantillaPruebaParcial = new File(path + "Plantilla_PruebaParcial.doc");

            TipoConstancia pruebaParcial = new TipoConstancia("PRUEBA PARCIAL");

            tipoConstanciaDAO.create(pruebaParcial, plantillaPruebaParcial);

            File plantillaJornadaExterna = new File(path + "Plantilla_JornadaExterna.doc");

            TipoConstancia jornadaExterna = new TipoConstancia("JORNADA EXTERNA");

            tipoConstanciaDAO.create(jornadaExterna, plantillaJornadaExterna);

            File plantillaEstudianteActivo = new File(path + "Plantilla_EstudianteActivo.doc");

            TipoConstancia estudianteActivo = new TipoConstancia("ESTUDIANTE ACTIVO");

            tipoConstanciaDAO.create(estudianteActivo, plantillaEstudianteActivo);

            File plantillaExamen = new File(path + "Plantilla_Examen.doc");

            TipoConstancia examen = new TipoConstancia("EXAMEN");

            tipoConstanciaDAO.create(examen, plantillaExamen);
        } catch (ServicioException ex) {
        }
    }
}
