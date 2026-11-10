package model;

import java.sql.Date;

public class Bloque {
    private int id_bloque;
    private String nombre;
    private Date fecha_inicio;
    private Date fecha_fin;

    public Bloque() {}

    public Bloque(int id, String nom, Date fi, Date ff) {
        this.id_bloque = id;
        this.nombre = nom;
        this.fecha_inicio = fi;
        this.fecha_fin = ff;
    }

    public int getIdBloque() { return this.id_bloque; }
    public void setIdBloque(int id) { this.id_bloque = id; }

    public String getNombre() { return this.nombre; }
    public void setNombre(String nom) { this.nombre = nom; }

    public Date getFechaInicio() { return this.fecha_inicio; }
    public void setFechaInicio(Date fi) { this.fecha_inicio = fi; }

    public Date getFechaFin() { return this.fecha_fin; }
    public void setFechaFin(Date ff) { this.fecha_fin = ff; }
}