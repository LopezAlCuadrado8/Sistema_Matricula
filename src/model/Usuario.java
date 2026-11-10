package model;

public class Usuario {
    private int id_usuario;
    private String dni;
    private String nombres;
    private String apellidos;
    private String correo;

    public Usuario() {}

    public Usuario(int id, String dni, String nom, String ape, String cor) {
        this.id_usuario = id;
        this.dni = dni;
        this.nombres = nom;
        this.apellidos = ape;
        this.correo = cor;
    }

    public int getIdUsuario() { return this.id_usuario; }
    public void setIdUsuario(int id) { this.id_usuario = id; }

    public String getDNI() { return this.dni; }
    public void setDNI(String dni) { this.dni = dni; }

    public String getNombres() { return this.nombres; }
    public void setNombres(String nom) { this.nombres = nom; }

    public String getApellidos() { return this.apellidos; }
    public void setApellidos(String ape) { this.apellidos = ape; }

    public String getCorreo() { return this.correo; }
    public void setCorreo(String cor) { this.correo = cor; }
}