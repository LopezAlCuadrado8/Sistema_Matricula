package model;

public class Aula {
    private int id_aula;
    private String nombre;
    private int capacidad;

    public Aula() {}

    public Aula(int id, String nom, int cap) {
        this.id_aula = id;
        this.nombre = nom;
        this.capacidad = cap;
    }

    public int getIdAula() { return this.id_aula; }
    public void setIdAula(int id) { this.id_aula = id; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nom) { this.nombre = nom; }

    public int getCapacidad() { return this.capacidad; }
    public void setCapacidad(int cap) { this.capacidad = cap; }
}