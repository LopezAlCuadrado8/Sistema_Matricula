package model;

public class Carrera {
    private int id_carrera;
    private String nombre;
    private int duracion_ciclos;

    public Carrera() {}

    public Carrera(int id, String nom, int dur) {
        this.id_carrera = id;
        this.nombre = nom;
        this.duracion_ciclos = dur;
    }

    public int getIdCarrera() { return this.id_carrera; }
    public void setIdCarrera(int id) { this.id_carrera = id; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nom) { this.nombre = nom; }

    public int getDuracionCiclos() { return this.duracion_ciclos; }
    public void setDuracionCiclos(int dur) { this.duracion_ciclos = dur; }

    @Override
    public String toString() { return this.nombre; }
}