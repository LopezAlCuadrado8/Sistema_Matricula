package model;

public class Docente {
    private int id_usuario;
    private String especialidad;
    private String grado_academico;

    public Docente() {}

    public Docente(int id, String esp, String grado) {
        this.id_usuario = id;
        this.especialidad = esp;
        this.grado_academico = grado;
    }

    public int getIdUsuario() { return this.id_usuario; }
    public void setIdUsuario(int id) { this.id_usuario = id; }

    public String getEspecialidad() { return this.especialidad; }
    public void setEspecialidad(String esp) { this.especialidad = esp; }

    public String getGradoAcademico() { return this.grado_academico; }
    public void setGradoAcademico(String grado) { this.grado_academico = grado; }
}