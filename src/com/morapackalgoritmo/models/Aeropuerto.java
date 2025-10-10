package com.morapackalgoritmo.models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Aeropuerto {

    // === Atributos ===
    private String codigo;          // Ej: "SKBO"
    private String nombre;          // Ej: "Bogotá"
    private String pais;            // Ej: "Colombia"
    private int capacidad;          // Capacidad máxima del almacén
    private int capacidadActual;    // Capacidad actual del almacén
    private int husoHorario;        // Ej: -5
    private String continente;      // Ej: "América"
    private List<ProductoEnAlmacen> productosActuales;

    // === Constructores ===
    public Aeropuerto() {
        this.productosActuales = new ArrayList<>();
    }

    public Aeropuerto(String codigo, String nombre, String pais, int capacidad, int husoHorario, String continente) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.pais = pais;
        this.capacidad = capacidad;
        this.capacidadActual = 0; // por defecto empieza vacío
        this.husoHorario = husoHorario;
        this.continente = continente;
        this.productosActuales = new ArrayList<>();
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

    public List<ProductoEnAlmacen> getProductosActuales() {
        return productosActuales;
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

    /**
     * Limpia productos expirados del almacén
     * @param momentoReferencia Momento actual para evaluar expiración
     */
    public void limpiarProductosExpirados(LocalDateTime momentoReferencia) {
        Iterator<ProductoEnAlmacen> iterator = productosActuales.iterator();

        while (iterator.hasNext()) {
            ProductoEnAlmacen producto = iterator.next();
            boolean debeEliminar = false;

            if (producto.esDestinoFinal()) {
                // Almacén de destino: eliminar si pasaron más de 2 horas
                Duration tiempoEnAlmacen = Duration.between(producto.getHoraLlegada(), momentoReferencia);
                if (tiempoEnAlmacen.toHours() >= 2) {
                    debeEliminar = true;
                }
            } else {
                // Almacén de tránsito: eliminar si el siguiente vuelo ya salió
                if (producto.getSiguienteVuelo().getHoraSalida().isBefore(momentoReferencia)) {
                    debeEliminar = true;
                }
            }

            if (debeEliminar) {
                capacidadActual -= producto.getCantidad();
                iterator.remove();
            }
        }
    }

    /**
     * Intenta agregar productos al almacén
     * @param producto Producto a agregar
     * @param momentoReferencia Momento de llegada
     * @return true si se pudo agregar, false si no hay espacio
     */
    public boolean agregarProductoAlAlmacen(ProductoEnAlmacen producto, LocalDateTime momentoReferencia) {
        // Primero limpiar productos expirados
        limpiarProductosExpirados(momentoReferencia);

        // Verificar si hay espacio
        if (capacidadActual + producto.getCantidad() <= capacidad) {
            productosActuales.add(producto);
            capacidadActual += producto.getCantidad();
            return true;
        }

        return false; // No hay espacio
    }

    /**
     * Calcula la capacidad disponible actual (después de limpiar expirados)
     * @param momentoReferencia Momento para evaluar
     * @return Capacidad disponible
     */

}
