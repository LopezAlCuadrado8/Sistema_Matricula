package model;

public class DetalleMatricula {
    private int id_matricula;
    private int id_curso;
    private Double nota;
    private int estado;

    public DetalleMatricula() {}

    public DetalleMatricula(int idMat, int idCur, Double nota, int est) {
        this.id_matricula = idMat;
        this.id_curso = idCur;
        this.nota = nota;
        this.estado = est;
    }

    public int getIdMatricula() { return this.id_matricula; }
    public void setIdMatricula(int idMat) { this.id_matricula = idMat; }

    public int getIdCurso() { return this.id_curso; }
    public void setIdCurso(int idCur) { this.id_curso = idCur; }

    public Double getNota() { return this.nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public int getEstado() { return this.estado; }
    public void setEstado(int est) { this.estado = est; }
}