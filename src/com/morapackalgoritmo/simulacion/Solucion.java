package com.morapackalgoritmo.simulacion;
import com.morapackalgoritmo.models.*;

import java.util.ArrayList;
import java.util.List;

public class Solucion {

    // === Atributos ===
    private List<Ruta> rutas;                    // Todas las rutas asignadas
    private double fitness;                       // Valor de calidad de la solución
    private int pedidosEntregadosATiempo;        // Contador para objetivo 1
    private int violacionesCapacidadVuelos;      // Contador para objetivo 2 (vuelos)
    private int violacionesCapacidadAlmacenes;   // Contador para objetivo 2 (almacenes)

    // === Constructores ===
    public Solucion() {
        this.rutas = new ArrayList<>();
        this.fitness = 0.0;
        this.pedidosEntregadosATiempo = 0;
        this.violacionesCapacidadVuelos = 0;
        this.violacionesCapacidadAlmacenes = 0;
    }

    public Solucion(List<Ruta> rutas) {
        this.rutas = (rutas != null) ? rutas : new ArrayList<>();
        this.fitness = 0.0;
        this.pedidosEntregadosATiempo = 0;
        this.violacionesCapacidadVuelos = 0;
        this.violacionesCapacidadAlmacenes = 0;
    }

    // === Getters y Setters ===
    public List<Ruta> getRutas() {
        return rutas;
    }

    public void setRutas(List<Ruta> rutas) {
        this.rutas = rutas;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getPedidosEntregadosATiempo() {
        return pedidosEntregadosATiempo;
    }

    public void setPedidosEntregadosATiempo(int pedidosEntregadosATiempo) {
        this.pedidosEntregadosATiempo = pedidosEntregadosATiempo;
    }

    public int getViolacionesCapacidadVuelos() {
        return violacionesCapacidadVuelos;
    }

    public void setViolacionesCapacidadVuelos(int violacionesCapacidadVuelos) {
        this.violacionesCapacidadVuelos = violacionesCapacidadVuelos;
    }

    public int getViolacionesCapacidadAlmacenes() {
        return violacionesCapacidadAlmacenes;
    }

    public void setViolacionesCapacidadAlmacenes(int violacionesCapacidadAlmacenes) {
        this.violacionesCapacidadAlmacenes = violacionesCapacidadAlmacenes;
    }

    // === Métodos funcionales ===

    /**
     * Agrega una ruta a la solución.
     */
    public void agregarRuta(Ruta ruta) {
        if (ruta != null) {
            rutas.add(ruta);
        }
    }

    /**
     * Retorna el número total de rutas en la solución.
     */
    public int getNumeroDeRutas() {
        return rutas.size();
    }

    @Override
    public String toString() {
        return "Solucion{" +
                "numRutas=" + rutas.size() +
                ", fitness=" + fitness +
                ", pedidosATiempo=" + pedidosEntregadosATiempo +
                ", violacionesVuelos=" + violacionesCapacidadVuelos +
                ", violacionesAlmacenes=" + violacionesCapacidadAlmacenes +
                '}';
    }
}