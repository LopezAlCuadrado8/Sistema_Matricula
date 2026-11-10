package model;

import java.sql.Date;

public class Matricula {
    private int id_matricula;
    private Date fecha;
    private int estado;
    private int id_estudiante;
    private int id_bloque;

    public Matricula() {}

    public Matricula(int id, Date f, int est, int idEst, int idBlo) {
        this.id_matricula = id;
        this.fecha = f;
        this.estado = est;
        this.id_estudiante = idEst;
        this.id_bloque = idBlo;
    }

    public int getIdMatricula() { return this.id_matricula; }
    public void setIdMatricula(int id) { this.id_matricula = id; }

    public Date getFecha() { return this.fecha; }
    public void setFecha(Date f) { this.fecha = f; }

    public int getEstado() { return this.estado; }
    public void setEstado(int est) { this.estado = est; }

    public int getIdEstudiante() { return this.id_estudiante; }
    public void setIdEstudiante(int idEst) { this.id_estudiante = idEst; }

    public int getIdBloque() { return this.id_bloque; }
    public void setIdBloque(int idBlo) { this.id_bloque = idBlo; }
}