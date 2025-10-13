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
     * Intenta agregar productos al almacén
     * @param producto Producto a agregar
     * @param momentoReferencia Momento de llegada
     * @return true si se pudo agregar, false si no hay espacio
     */
    public boolean agregarProductoAlAlmacen(ProductoEnAlmacen producto, LocalDateTime momento) {
        // Validar si hay espacio en ese momento
        if (hayEspacioEnMomento(producto.getCantidad(), momento)) {
            productosActuales.add(producto); // Solo agregar, nunca eliminar
            return true;
        }
        return false;
    }

    /**
     * Calcula la capacidad disponible actual (después de limpiar expirados)
     * @param momentoReferencia Momento para evaluar
     * @return Capacidad disponible
     */

    public int calcularOcupacionEnMomento(LocalDateTime momento) {
        int ocupacion = 0;

        for (ProductoEnAlmacen producto : productosActuales) {
            boolean estaPresente = false;

            if (producto.esDestinoFinal()) {
                // Destino: está presente si no han pasado 2 horas Y ya llegó
                Duration tiempo = Duration.between(producto.getHoraLlegada(), momento);
                if (tiempo.toHours() >= 0 && tiempo.toHours() <= 2) {
                    estaPresente = true;
                }
            } else {
                // Tránsito: está presente si el siguiente vuelo NO ha salido Y ya llegó
                LocalDateTime llegada = producto.getHoraLlegada();
                LocalDateTime salida = producto.getSiguienteVuelo().getHoraSalida();

                if ((momento.isAfter(llegada) || momento.isEqual(llegada)) &&
                        (momento.isBefore(salida) || momento.isEqual(salida))) {
                    estaPresente = true;
                }
            }

            if (estaPresente) {
                ocupacion += producto.getCantidad();
            }
        }

        return ocupacion;
    }

    /**
     * Verifica si hay espacio para agregar productos en un momento dado
     */
    public boolean hayEspacioEnMomento(int cantidadAAgregar, LocalDateTime momento) {
        int ocupacionActual = calcularOcupacionEnMomento(momento);
        return (ocupacionActual + cantidadAAgregar) <= capacidad;
    }

    public void imprimirEstadoEnMomento(LocalDateTime momento) {
        int ocupacion = calcularOcupacionEnMomento(momento);
        int disponible = capacidad - ocupacion;

        System.out.println("\n📍 Estado de " + nombre + " (" + codigo + ") en " + momento);
        System.out.println("   Capacidad: " + ocupacion + "/" + capacidad +
                " (Disponible: " + disponible + ")");

        // Listar productos presentes en ese momento
        System.out.println("   Productos presentes:");

        int count = 0;
        for (ProductoEnAlmacen producto : productosActuales) {
            boolean estaPresente = false;

            if (producto.esDestinoFinal()) {
                Duration tiempo = Duration.between(producto.getHoraLlegada(), momento);
                // ❌ FALTA: tiempo.toHours() >= 0
                if (tiempo.toHours() < 2 && !tiempo.isNegative()) { // isNegative() es equivalente pero menos claro
                    estaPresente = true;
                }
            } else {
                // ✅ CORRECTO: ya tiene ambas validaciones
                if (!producto.getSiguienteVuelo().getHoraSalida().isBefore(momento) &&
                        !producto.getHoraLlegada().isAfter(momento)) {
                    estaPresente = true;
                }
            }

            if (estaPresente) {
                count++;
                String tipo = producto.esDestinoFinal() ? "DESTINO" : "TRÁNSITO";
                System.out.println("      " + count + ". " + producto.getCantidad() + " productos - " +
                        "Llegada: " + producto.getHoraLlegada().toLocalTime() +
                        " - Tipo: " + tipo +
                        " - Pedido: " + producto.getRuta().getPedido().getIdCliente());
            }
        }

        if (count == 0) {
            System.out.println("      (vacío)");
        }
    }


}
