
package controller;

import java.util.regex.Pattern;

public class ValidacionController {

    // Validar campos vacíos
    public boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // Validar DNI: exactamente 8 números
    public boolean validarDNI(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }

    // Validar nombres y apellidos: solo letras y espacios
    public boolean validarNombre(String nombre) {
        return nombre != null &&
               nombre.matches("[\\p{L}]+([ '\\-][\\p{L}]+)*");
    }

    // Validar correo electrónico
    public boolean validarCorreo(String correo) {
        if (correo == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return Pattern.matches(regex, correo);
    }

    // Validar teléfono: 9 números
    public boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("\\d{9}");
    }

    // Validar longitud de texto
    public boolean validarLongitud(String texto, int minimo, int maximo) {
        return texto != null &&
               texto.length() >= minimo &&
               texto.length() <= maximo;
    }

    // Validar contraseña
    public boolean validarPassword(String password) {
        return password != null && password.length() >= 8;
    }

    // Comparar contraseña y confirmación
    public boolean coincidenPassword(String password, String confirmacion) {
        return password != null &&
               password.equals(confirmacion);
    }
}
