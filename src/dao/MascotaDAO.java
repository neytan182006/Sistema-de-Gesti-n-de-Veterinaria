package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MascotaDAO {

    public void listar() throws SQLException {
        String sql = "SELECT m.IdMascota, m.Nombre, m.Especie, m.Raza, m.Edad, d.Nombre AS Dueno "
                + "FROM MASCOTAS m INNER JOIN DUENOS d ON m.IdDueno = d.IdDueno ORDER BY m.Nombre";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.printf("[%d] %-15s %-10s %-15s %d anios | Dueno: %s%n",
                        rs.getInt("IdMascota"), rs.getString("Nombre"), rs.getString("Especie"),
                        rs.getString("Raza"), rs.getInt("Edad"), rs.getString("Dueno"));
            }
        }
    }
}
