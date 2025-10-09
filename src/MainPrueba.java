

import com.morapackalgoritmo.models.*;
import com.morapackalgoritmo.simulacion.GRASP;
import com.morapackalgoritmo.simulacion.Solucion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainPrueba {

    public static void main(String[] args) {
        System.out.println("=== PRUEBA GRASP ===\n");

        // 1. Crear aeropuertos
        List<Aeropuerto> aeropuertos = crearAeropuertos();

        // 2. Identificar sedes principales
        List<Aeropuerto> sedesPrincipales = new ArrayList<>();
        sedesPrincipales.add(buscarAeropuerto(aeropuertos, "SPJC")); // Lima
        sedesPrincipales.add(buscarAeropuerto(aeropuertos, "EBBR")); // Bruselas

        // 3. Crear vuelos (simulando una semana)
        List<Vuelo> vuelos = crearVuelos(aeropuertos);

        // 4. Crear pedidos
        List<Pedido> pedidos = crearPedidos();

        // 5. Inicializar GRASP
        GRASP grasp = new GRASP(pedidos, vuelos, aeropuertos, sedesPrincipales, 0.3, 2);

        System.out.println("Configuración:");
        System.out.println("- Aeropuertos: " + aeropuertos.size());
        System.out.println("- Vuelos: " + vuelos.size());
        System.out.println("- Pedidos: " + pedidos.size());
        System.out.println("- Sedes principales: " + sedesPrincipales.size());
        System.out.println("\n=== EJECUTANDO GRASP ===\n");

        // 6. Generar solución
        Solucion solucion = grasp.generarSolucion();

        // 7. Mostrar resultados
        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Rutas creadas: " + solucion.getNumeroDeRutas());

        for (Ruta ruta : solucion.getRutas()) {
            System.out.println("\n" + ruta);
        }

        // 8. Verificar capacidades de vuelos
        System.out.println("\n=== ESTADO DE VUELOS ===");
        for (Vuelo vuelo : vuelos) {
            if (vuelo.getCapacidadActual() > 0) {
                System.out.println(vuelo.getAeropuertoOrigen().getCodigo() + "→" +
                        vuelo.getAeropuertoDestino().getCodigo() +
                        ": " + vuelo.getCapacidadActual() + "/" +
                        vuelo.getCapacidadMaxima());
            }
        }
    }

    // === Métodos auxiliares para crear datos de prueba ===

    private static List<Aeropuerto> crearAeropuertos() {
        List<Aeropuerto> aeropuertos = new ArrayList<>();

        // América
        aeropuertos.add(new Aeropuerto("SPJC", "Lima", "Peru", 800, -5, "America"));
        aeropuertos.add(new Aeropuerto("SKBO", "Bogota", "Colombia", 700, -5, "America"));
        aeropuertos.add(new Aeropuerto("SAEZ", "BuenosAires", "Argentina", 900, -3, "America"));
        aeropuertos.add(new Aeropuerto("SCEL", "Santiago", "Chile", 750, -4, "America"));

        // Europa
        aeropuertos.add(new Aeropuerto("EBBR", "Bruselas", "Belgica", 1000, 1, "Europa"));
        aeropuertos.add(new Aeropuerto("LEMD", "Madrid", "España", 850, 1, "Europa"));
        aeropuertos.add(new Aeropuerto("LFPG", "Paris", "Francia", 900, 1, "Europa"));

        return aeropuertos;
    }

    private static List<Vuelo> crearVuelos(List<Aeropuerto> aeropuertos) {
        List<Vuelo> vuelos = new ArrayList<>();

        Aeropuerto lima = buscarAeropuerto(aeropuertos, "SPJC");
        Aeropuerto bogota = buscarAeropuerto(aeropuertos, "SKBO");
        Aeropuerto buenosAires = buscarAeropuerto(aeropuertos, "SAEZ");
        Aeropuerto santiago = buscarAeropuerto(aeropuertos, "SCEL");
        Aeropuerto bruselas = buscarAeropuerto(aeropuertos, "EBBR");
        Aeropuerto madrid = buscarAeropuerto(aeropuertos, "LEMD");
        Aeropuerto paris = buscarAeropuerto(aeropuertos, "LFPG");

        // Vuelos día 1 (capacidad limitada para probar)
        // Lima → Bogotá → Buenos Aires (ruta con escala)
        vuelos.add(new Vuelo(lima, bogota,
                LocalDateTime.of(2025, 1, 1, 8, 0),
                LocalDateTime.of(2025, 1, 1, 11, 0),
                50)); // Capacidad pequeña

        vuelos.add(new Vuelo(bogota, buenosAires,
                LocalDateTime.of(2025, 1, 1, 12, 0),
                LocalDateTime.of(2025, 1, 1, 20, 0),
                50)); // Capacidad pequeña

        // Lima → Santiago → Buenos Aires (ruta alternativa)
        vuelos.add(new Vuelo(lima, santiago,
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 1, 1, 13, 0),
                60));

        vuelos.add(new Vuelo(santiago, buenosAires,
                LocalDateTime.of(2025, 1, 1, 16, 0),
                LocalDateTime.of(2025, 1, 1, 19, 0),
                60));

        // Bruselas → París → Madrid (Europa)
        vuelos.add(new Vuelo(bruselas, paris,
                LocalDateTime.of(2025, 1, 1, 9, 0),
                LocalDateTime.of(2025, 1, 1, 10, 30),
                100));

        vuelos.add(new Vuelo(paris, madrid,
                LocalDateTime.of(2025, 1, 1, 13, 0),
                LocalDateTime.of(2025, 1, 2, 15, 0),
                100));

        return vuelos;
    }

    private static List<Pedido> crearPedidos() {
        List<Pedido> pedidos = new ArrayList<>();

        // Pedido grande: 120 productos a Buenos Aires
        // Debería dividirse en múltiples rutas porque ningún vuelo tiene capacidad
        pedidos.add(new Pedido(1, 6, 0, "SAEZ", 120, "0001234"));

        // Pedido pequeño: 30 productos a Madrid
        pedidos.add(new Pedido(1, 7, 0, "LEMD", 30, "0005678"));

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