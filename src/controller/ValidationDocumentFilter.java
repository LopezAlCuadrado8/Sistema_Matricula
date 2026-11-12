
package controller;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ValidationDocumentFilter extends DocumentFilter {

    private final String tipo;
    private final int longitudMaxima;

    public ValidationDocumentFilter(String tipo, int longitudMaxima) {
        this.tipo = tipo;
        this.longitudMaxima = longitudMaxima;
    }

    // Comprueba si el texto cumple las reglas
    private boolean esValido(String texto) {

        switch (tipo) {

            case "NUMEROS":
                return texto.matches("\\d*");

            case "LETRAS":
                return texto.matches("[\\p{L} '\\-]*");

            case "ALFANUMERICO":
                return texto.matches("[\\p{L}\\d .,\\-#]*");
 
            case "DECIMAL":
                return texto.matches("\\d*(\\.\\d{0,2})?");
 
            case "CORREO":
                return texto.matches("[A-Za-z0-9._%+@-]*");

            default:
                return true;
        }
    }

    // Controla cuando se inserta texto
    @Override
    public void insertString(
            FilterBypass fb,
            int offset,
            String texto,
            AttributeSet attr) throws BadLocationException {

        replace(fb, offset, 0, texto, attr);
    }

    // Controla cuando se reemplaza o modifica texto
    @Override
    public void replace(
            FilterBypass fb,
            int offset,
            int length,
            String texto,
            AttributeSet attrs) throws BadLocationException {

        if (texto == null) {
            return;
        }

        String textoActual = fb.getDocument().getText(
                0, fb.getDocument().getLength());

        String nuevoTexto = textoActual.substring(0, offset)
                + texto
                + textoActual.substring(offset + length);

        if (nuevoTexto.length() <= longitudMaxima
                && esValido(nuevoTexto)) {

            fb.replace(offset, length, texto, attrs);
        }
    }

    // Permite eliminar texto seleccionado
    @Override
    public void remove(
            FilterBypass fb,
            int offset,
            int length) throws BadLocationException {

        fb.remove(offset, length);
    }
}
