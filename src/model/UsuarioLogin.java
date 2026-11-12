
package model;

public class UsuarioLogin {

    private int idUsuario;
    private String nombres;
    private String apellidos;
    private String correo;
    private String rol;

    public UsuarioLogin(int idUsuario, String nombres,
                        String apellidos, String correo, String rol) {
        this.idUsuario = idUsuario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }
}
