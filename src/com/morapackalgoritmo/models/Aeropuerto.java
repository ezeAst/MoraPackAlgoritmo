package com.morapackalgoritmo.models;

public class Aeropuerto {

    // === Atributos ===
    private String codigo;          // Ej: "SKBO"
    private String nombre;          // Ej: "Bogotá"
    private String pais;            // Ej: "Colombia"
    private int capacidad;          // Capacidad máxima del almacén
    private int capacidadActual;    // Capacidad actual del almacén
    private int husoHorario;        // Ej: -5
    private String continente;      // Ej: "América"

    // === Constructores ===
    public Aeropuerto() {
    }

    public Aeropuerto(String codigo, String nombre, String pais, int capacidad, int husoHorario, String continente) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.capacidad = capacidad;
        this.capacidadActual = 0; // por defecto empieza vacío
        this.husoHorario = husoHorario;
        this.continente = continente;
    }

    // === Getters y Setters ===
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getCapacidadActual() {
        return capacidadActual;
    }

    public void setCapacidadActual(int capacidadActual) {
        this.capacidadActual = capacidadActual;
    }

    public int getHusoHorario() {
        return husoHorario;
    }

    public void setHusoHorario(int husoHorario) {
        this.husoHorario = husoHorario;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

    // === Métodos funcionales ===
    public boolean agregarCarga(int cantidad) {
        if (capacidadActual + cantidad <= capacidad) {
            capacidadActual += cantidad;
            return true;
        } else {
            return false; // no hay espacio suficiente
        }
    }

    public boolean retirarCarga(int cantidad) {
        if (capacidadActual - cantidad >= 0) {
            capacidadActual -= cantidad;
            return true;
        } else {
            return false; // no hay suficiente carga para retirar
        }
    }

    public boolean estaLleno() {
        return capacidadActual >= capacidad;
    }

    @Override
    public String toString() {
        return "Aeropuerto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", pais='" + pais + '\'' +
                ", capacidad=" + capacidad +
                ", capacidadActual=" + capacidadActual +
                ", husoHorario=" + husoHorario +
                ", continente='" + continente + '\'' +
                '}';
    }
}
