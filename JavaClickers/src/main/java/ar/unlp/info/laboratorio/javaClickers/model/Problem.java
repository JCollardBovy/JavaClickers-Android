package ar.unlp.info.laboratorio.javaClickers.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jony on 05/07/13.
 */
public class Problem implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 189L;

    /**
     * El enunciado del problema a resolver
     */
    private String assigment;

    /**
     * Tiempo definido para resolver el problema
     */
    private long time;

    /**
     * Posibles solutions del problema
     */
    private Collection<Solution> solutions;

    /**
     * Constructor que genera un nuevo problema a partir de una assigment y un conjunto de posibles solutions
     * @param assigment		Enunciado del problema
     * @param solutions	Posibles solutions del problema
     */
    public Problem(String assigment, Collection<Solution> solutions) {
        super();
        this.assigment = assigment;
        this.solutions = solutions;
    }

    /**
     * Constructor que genera un nuevo problema a partir de una assigment, un time y un conjunto de posibles solutions
     * @param assigment		Enunciado del problema
     * @param time		Tiempo para resolver el problema
     * @param solutions	Posibles solutions del problema
     */
    public Problem(String assigment, long time, Collection<Solution> solutions) {
        this(assigment, solutions);
        this.time = time;
    }

    /**
     * Retorna el enunciado a resolver
     * @return	Enunciado del problema
     */
    public String getAssigment() {
        return assigment;
    }

    /**
     * Retorna el conjunto de solutions al problema
     * @return	Coleccion con solutions al problema
     */
    public Collection<Solution> getSolutions() {
        return solutions;
    }

    /**
     * Retorna el time definido para resolver el problema
     * @return	Numero entero con el time en segundos
     */
    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Problem)) return false;

        Problem problem = (Problem) o;

        if (time != problem.time) return false;
        if (!assigment.equals(problem.assigment)) return false;
        if (!solutions.equals(problem.solutions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = assigment.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + solutions.hashCode();
        return result;
    }
}
