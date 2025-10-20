import com.morapackalgoritmo.models.*;
import com.morapackalgoritmo.simulacion.GRASP;
import com.morapackalgoritmo.simulacion.Planificador;
import com.morapackalgoritmo.simulacion.Solucion;
import com.morapackalgoritmo.utils.LectorCSV;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainConCSV {

    public static void main(String[] args) {
        System.out.println("=== CARGANDO DATOS DESDE CSV ===\n");

        // 1. Leer aeropuertos
        List<Aeropuerto> aeropuertos = LectorCSV.leerAeropuertos("data/aeropuertos.csv");

        // 2. Identificar sedes principales (Lima, Bruselas, Baku)
        List<String> codigosSedes = List.of("SPIM", "EBCI", "UBBB");
        List<Aeropuerto> sedesPrincipales = LectorCSV.identificarSedesPrincipales(aeropuertos, codigosSedes);

        // 3. Leer vuelos (genera 7 instancias automáticamente)
        List<Vuelo> vuelos = LectorCSV.leerVuelos("data/vuelos.txt", aeropuertos);

        // 4. Leer pedidos
        List<Pedido> pedidos = LectorCSV.leerPedidos("data/pedidos_m.txt");

        System.out.println("\n=== DATOS CARGADOS CORRECTAMENTE ===\n");

        // 5. Crear planificador y ejecutar
        Planificador planificador = new Planificador(pedidos, vuelos, aeropuertos, sedesPrincipales);
        Solucion solucion = planificador.ejecutarPlanificacion();

        // 6. Mostrar resultados
        System.out.println("\n=== SOLUCIÓN FINAL ===");
        System.out.println(solucion);
    }
}