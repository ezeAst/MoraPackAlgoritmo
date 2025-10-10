
import com.morapackalgoritmo.models.*;
import com.morapackalgoritmo.simulacion.GRASP;
import com.morapackalgoritmo.simulacion.Solucion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainPruebaAlmacenes {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA DE CAPACIDADES DE ALMACENES ===\n");

        // 1. Crear aeropuertos con CAPACIDADES LIMITADAS
        List<Aeropuerto> aeropuertos = crearAeropuertos();

        // 2. Identificar sedes principales
        List<Aeropuerto> sedesPrincipales = new ArrayList<>();
        sedesPrincipales.add(buscarAeropuerto(aeropuertos, "SPJC")); // Lima

        // 3. Crear vuelos
        List<Vuelo> vuelos = crearVuelos(aeropuertos);

        // 4. Crear pedidos (diseñados para saturar almacenes)
        List<Pedido> pedidos = crearPedidos();

        // 5. Mostrar configuración inicial
        System.out.println("=== CONFIGURACIÓN INICIAL ===");
        for (Aeropuerto a : aeropuertos) {
            System.out.println(a.getCodigo() + " - Capacidad almacén: " + a.getCapacidad());
        }
        System.out.println("\nPedidos: " + pedidos.size());
        System.out.println();

        // 6. Inicializar GRASP
        GRASP grasp = new GRASP(pedidos, vuelos, aeropuertos, sedesPrincipales, 0.3, 2);

        System.out.println("=== EJECUTANDO GRASP ===\n");

        // 7. Generar solución
        Solucion solucion = grasp.generarSolucion();

        // 8. Mostrar resultados
        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Rutas creadas: " + solucion.getNumeroDeRutas());

        System.out.println("\n=== RUTAS DETALLADAS ===");
        for (Ruta ruta : solucion.getRutas()) {
            System.out.println(ruta);
        }

        // 9. Verificar estado de VUELOS
        System.out.println("\n=== ESTADO DE VUELOS ===");
        for (Vuelo vuelo : vuelos) {
            if (vuelo.getCapacidadActual() > 0) {
                System.out.println(vuelo.getAeropuertoOrigen().getCodigo() + "→" +
                        vuelo.getAeropuertoDestino().getCodigo() +
                        ": " + vuelo.getCapacidadActual() + "/" +
                        vuelo.getCapacidadMaxima());
            }
        }

        // 10. Verificar estado de ALMACENES
        System.out.println("\n=== ESTADO DE ALMACENES ===");
        for (Aeropuerto aeropuerto : aeropuertos) {
            if (aeropuerto.getCapacidadActual() > 0 || !aeropuerto.getProductosActuales().isEmpty()) {
                System.out.println("\n" + aeropuerto.getCodigo() + " (" + aeropuerto.getNombre() + "):");
                System.out.println("  Capacidad: " + aeropuerto.getCapacidadActual() + "/" +
                        aeropuerto.getCapacidad());
                System.out.println("  Productos en almacén: " + aeropuerto.getProductosActuales().size());

                for (ProductoEnAlmacen producto : aeropuerto.getProductosActuales()) {
                    System.out.println("    - " + producto.getCantidad() + " productos, " +
                            "llegada: " + producto.getHoraLlegada() + ", " +
                            "tipo: " + (producto.esDestinoFinal() ? "DESTINO FINAL" : "TRÁNSITO"));
                }
            }
        }
    }

    // === Métodos auxiliares ===

    private static List<Aeropuerto> crearAeropuertos() {
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        // América - CAPACIDADES LIMITADAS para forzar validación
        aeropuertos.add(new Aeropuerto("SPJC", "Lima", "Peru", 100, -5, "America")); // Capacidad baja
        aeropuertos.add(new Aeropuerto("SKBO", "Bogota", "Colombia", 80, -5, "America")); // Capacidad baja
        aeropuertos.add(new Aeropuerto("SAEZ", "BuenosAires", "Argentina", 120, -3, "America")); // Destino
        aeropuertos.add(new Aeropuerto("SCEL", "Santiago", "Chile", 90, -4, "America"));

        return aeropuertos;
    }

    private static List<Vuelo> crearVuelos(List<Aeropuerto> aeropuertos) {
        List<Vuelo> vuelos = new ArrayList<>();

        Aeropuerto lima = buscarAeropuerto(aeropuertos, "SPJC");
        Aeropuerto bogota = buscarAeropuerto(aeropuertos, "SKBO");
        Aeropuerto buenosAires = buscarAeropuerto(aeropuertos, "SAEZ");
        Aeropuerto santiago = buscarAeropuerto(aeropuertos, "SCEL");

        // Ruta 1: Lima → Bogotá → Buenos Aires (con escala en Bogotá)
        vuelos.add(new Vuelo(lima, bogota,
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0),
                100));

        vuelos.add(new Vuelo(bogota, buenosAires,
                LocalDateTime.of(2025, 1, 1, 14, 0),  // 3 horas de espera (válido)
                LocalDateTime.of(2025, 1, 1, 20, 0),
                100));

        // Ruta 2: Lima → Santiago → Buenos Aires (ruta alternativa)
        vuelos.add(new Vuelo(lima, santiago,
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 12, 0),
                100));

        vuelos.add(new Vuelo(santiago, buenosAires,
                LocalDateTime.of(2025, 1, 1, 15, 0),  // 3 horas de espera
                LocalDateTime.of(2025, 1, 1, 18, 0),
                100));

        // Vuelo adicional posterior para probar limpieza de productos
        vuelos.add(new Vuelo(lima, bogota,
                LocalDateTime.of(2025, 1, 1, 16, 0),  // Vuelo más tarde
                LocalDateTime.of(2025, 1, 1, 19, 0),
                100));

        return vuelos;
    }

    private static List<Pedido> crearPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        // Pedido 1: 70 productos a Buenos Aires (debe usar Bogotá como tránsito)
        // Saturará almacén de Bogotá (capacidad 80)
        pedidos.add(new Pedido(1, 6, 0, "SAEZ", 70, "0001111"));

        // Pedido 2: 60 productos a Buenos Aires (probará ruta alternativa por Santiago)
        // O intentará usar Bogotá pero debería estar saturado
        pedidos.add(new Pedido(1, 6, 30, "SAEZ", 60, "0002222"));

        // Pedido 3: 40 productos a Buenos Aires (más tarde, Bogotá ya debería estar libre)
        // Este pedido llega cuando el vuelo de conexión ya salió (productos limpiados)
        pedidos.add(new Pedido(1, 15, 0, "SAEZ", 40, "0003333"));

        return pedidos;
    }

    private static Aeropuerto buscarAeropuerto(List<Aeropuerto> aeropuertos, String codigo) {
        for (Aeropuerto a : aeropuertos) {
            if (a.getCodigo().equals(codigo)) {
                return a;
            }
        }
        return null;
    }
}