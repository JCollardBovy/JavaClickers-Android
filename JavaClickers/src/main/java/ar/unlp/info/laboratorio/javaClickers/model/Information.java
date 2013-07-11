package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;

/**
 * Created by Jony on 03/06/13.
 */
public class Information implements Serializable{

    public static final long serialVersionUID = 188L;
    
    private String asignatura;
    private String[] temas;

    public Information(String asignatura, String[] temas) {
        this.asignatura = asignatura;
        this.temas = temas;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public String[] getTemas() {
        return temas;
    }
}
