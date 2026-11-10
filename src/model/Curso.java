package model;

public class Curso {
    private int id_curso;
    private String nombre;
    private int creditos;
    private int horas;
    private int id_carrera;

    public Curso() {}

    public Curso(int id, String nom, int cred, int hor, int idCar) {
        this.id_curso = id;
        this.nombre = nom;
        this.creditos = cred;
        this.horas = hor;
        this.id_carrera = idCar;
    }

    public int getIdCurso() { return this.id_curso; }
    public void setIdCurso(int id) { this.id_curso = id; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nom) { this.nombre = nom; }

    public int getCreditos() { return this.creditos; }
    public void setCreditos(int cred) { this.creditos = cred; }

    public int getHoras() { return this.horas; }
    public void setHoras(int hor) { this.horas = hor; }

    public int getIdCarrera() { return this.id_carrera; }
    public void setIdCarrera(int idCar) { this.id_carrera = idCar; }
}