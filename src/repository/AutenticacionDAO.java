
package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.ConexionMySQL;
import model.UsuarioLogin;

import org.mindrot.jbcrypt.BCrypt;

public class AutenticacionDAO {

    public UsuarioLogin autenticar(String correo, String password)
            throws SQLException {

        String sql = "SELECT u.id_usuario, u.nombres, u.apellidos, " +
                     "u.correo, c.password_hash, c.rol " +
                     "FROM tb_usuario u " +
                     "INNER JOIN tb_credencial c " +
                     "ON u.id_usuario = c.id_usuario " +
                     "WHERE u.correo = ?";

        ConexionMySQL conexion = new ConexionMySQL();

        try (Connection cn = conexion.getConectar()) {

            if (cn == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, correo);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String passwordHash =
                                rs.getString("password_hash");

                        boolean passwordCorrecta =
                                BCrypt.checkpw(password, passwordHash);

                        if (passwordCorrecta) {

                            return new UsuarioLogin(
                                rs.getInt("id_usuario"),
                                rs.getString("nombres"),
                                rs.getString("apellidos"),
                                rs.getString("correo"),
                                rs.getString("rol")
                            );
                        }
                    }
                }
            }
        }

        return null;
    }
}
