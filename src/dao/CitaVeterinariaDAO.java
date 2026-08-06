package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CitaVeterinariaDAO {

    public int agendar(int idMascota, String fecha, String motivo) throws SQLException {
        String sql = "INSERT INTO CITAS_VETERINARIAS (IdMascota, Fecha, Motivo) VALUES (?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idMascota);
            ps.setString(2, fecha);
            ps.setString(3, motivo);
            ps.executeUpdate();
            try (ResultSet generadas = ps.getGeneratedKeys()) {
                generadas.next();
                return generadas.getInt(1);
            }
        }
    }

    /**
     * Marca la cita como atendida y registra el resultado en el historial
     * medico de la mascota, en una sola transaccion.
     */
    public boolean atenderCita(int idCita, String diagnostico, String tratamiento) throws SQLException {
        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                int idMascota;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT IdMascota FROM CITAS_VETERINARIAS WHERE IdCita = ? AND Estado = 'PENDIENTE'")) {
                    ps.setInt(1, idCita);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            return false;
                        }
                        idMascota = rs.getInt("IdMascota");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE CITAS_VETERINARIAS SET Estado = 'ATENDIDA' WHERE IdCita = ?")) {
                    ps.setInt(1, idCita);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO HISTORIAL_MEDICO (IdMascota, Diagnostico, Tratamiento) VALUES (?, ?, ?)")) {
                    ps.setInt(1, idMascota);
                    ps.setString(2, diagnostico);
                    ps.setString(3, tratamiento);
                    ps.executeUpdate();
                }

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void listarPendientes() throws SQLException {
        String sql = "SELECT c.IdCita, m.Nombre AS Mascota, c.Fecha, c.Motivo "
                + "FROM CITAS_VETERINARIAS c INNER JOIN MASCOTAS m ON c.IdMascota = m.IdMascota "
                + "WHERE c.Estado = 'PENDIENTE' ORDER BY c.Fecha";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean hayDatos = false;
            while (rs.next()) {
                hayDatos = true;
                System.out.printf("[%d] %-15s %s - %s%n",
                        rs.getInt("IdCita"), rs.getString("Mascota"), rs.getDate("Fecha"), rs.getString("Motivo"));
            }
            if (!hayDatos) {
                System.out.println("No hay citas pendientes.");
            }
        }
    }

    public void mostrarHistorial(int idMascota) throws SQLException {
        String sql = "SELECT Fecha, Diagnostico, Tratamiento FROM HISTORIAL_MEDICO "
                + "WHERE IdMascota = ? ORDER BY Fecha DESC";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMascota);
            try (ResultSet rs = ps.executeQuery()) {
                boolean hayDatos = false;
                while (rs.next()) {
                    hayDatos = true;
                    System.out.printf("%s | Diagnostico: %-30s | Tratamiento: %s%n",
                            rs.getDate("Fecha"), rs.getString("Diagnostico"), rs.getString("Tratamiento"));
                }
                if (!hayDatos) {
                    System.out.println("Esa mascota no tiene historial medico.");
                }
            }
        }
    }
}
