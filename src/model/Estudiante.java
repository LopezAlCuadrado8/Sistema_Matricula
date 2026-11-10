package model;

import java.sql.Date;

public class Estudiante {
    private int id_usuario;
    private String codigo;
    private Date fecha_ingreso;
    private int id_carrera;

    public Estudiante() {}

    public Estudiante(int id, String cod, Date fi, int idCar) {
        this.id_usuario = id;
        this.codigo = cod;
        this.fecha_ingreso = fi;
        this.id_carrera = idCar;
    }

    public int getIdUsuario() { return this.id_usuario; }
    public void setIdUsuario(int id) { this.id_usuario = id; }

    public String getCodigo() { return this.codigo; }
    public void setCodigo(String cod) { this.codigo = cod; }

    public Date getFechaIngreso() { return this.fecha_ingreso; }
    public void setFechaIngreso(Date fi) { this.fecha_ingreso = fi; }

    public int getIdCarrera() { return this.id_carrera; }
    public void setIdCarrera(int idCar) { this.id_carrera = idCar; }
}