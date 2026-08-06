package app;

import dao.CitaVeterinariaDAO;
import dao.MascotaDAO;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner TECLADO = new Scanner(System.in);
    private static final MascotaDAO mascotaDAO = new MascotaDAO();
    private static final CitaVeterinariaDAO citaDAO = new CitaVeterinariaDAO();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1 -> mascotaDAO.listar();
                    case 2 -> agendarCita();
                    case 3 -> atenderCita();
                    case 4 -> citaDAO.listarPendientes();
                    case 5 -> verHistorial();
                    case 0 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n=== GESTION DE VETERINARIA ===");
        System.out.println("1. Listar mascotas");
        System.out.println("2. Agendar cita");
        System.out.println("3. Atender cita (registra diagnostico en el historial)");
        System.out.println("4. Ver citas pendientes");
        System.out.println("5. Ver historial medico de una mascota");
        System.out.println("0. Salir");
    }

    private static void agendarCita() throws SQLException {
        int idMascota = leerEntero("Id de la mascota (ver opcion 1): ");
        System.out.print("Fecha (YYYY-MM-DD): ");
        String fecha = TECLADO.nextLine();
        System.out.print("Motivo: ");
        String motivo = TECLADO.nextLine();
        int idCita = citaDAO.agendar(idMascota, fecha, motivo);
        System.out.println("Cita agendada con id " + idCita);
    }

    private static void atenderCita() throws SQLException {
        int idCita = leerEntero("Id de la cita (ver opcion 4): ");
        System.out.print("Diagnostico: ");
        String diagnostico = TECLADO.nextLine();
        System.out.print("Tratamiento: ");
        String tratamiento = TECLADO.nextLine();
        boolean exito = citaDAO.atenderCita(idCita, diagnostico, tratamiento);
        System.out.println(exito ? "Cita atendida y registrada en el historial." : "No se encontro una cita pendiente con ese id.");
    }

    private static void verHistorial() throws SQLException {
        int idMascota = leerEntero("Id de la mascota: ");
        citaDAO.mostrarHistorial(idMascota);
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!TECLADO.hasNextInt()) {
            System.out.print("Ingrese un numero valido: ");
            TECLADO.next();
        }
        int valor = TECLADO.nextInt();
        TECLADO.nextLine();
        return valor;
    }
}
