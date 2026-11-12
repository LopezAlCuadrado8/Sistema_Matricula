
package controller;

import java.sql.SQLException;

import model.UsuarioLogin;
import repository.AutenticacionDAO;

public class LoginController {

    private AutenticacionDAO autenticacionDAO;

    public LoginController() {
        autenticacionDAO = new AutenticacionDAO();
    }

    public UsuarioLogin iniciarSesion(String correo, String password)
            throws SQLException {

        if (correo == null || correo.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return null;
        }

        return autenticacionDAO.autenticar(
            correo.trim(),
            password
        );
    }
}
