package model;

import java.sql.Time;

public class Horario {
    private int id_horario;
    private String dia;
    private Time hora_inicio;
    private Time hora_fin;
    private int id_curso;
    private int id_docente;
    private int id_aula;

    public Horario() {}

    public Horario(int id, String dia, Time hi, Time hf, int idCur, int idDoc, int idAul) {
        this.id_horario = id;
        this.dia = dia;
        this.hora_inicio = hi;
        this.hora_fin = hf;
        this.id_curso = idCur;
        this.id_docente = idDoc;
        this.id_aula = idAul;
    }

    public int getIdHorario() { return this.id_horario; }
    public void setIdHorario(int id) { this.id_horario = id; }

    public String getDia() { return this.dia; }
    public void setDia(String dia) { this.dia = dia; }

    public Time getHoraInicio() { return this.hora_inicio; }
    public void setHoraInicio(Time hi) { this.hora_inicio = hi; }

    public Time getHoraFin() { return this.hora_fin; }
    public void setHoraFin(Time hf) { this.hora_fin = hf; }

    public int getIdCurso() { return this.id_curso; }
    public void setIdCurso(int idCur) { this.id_curso = idCur; }

    public int getIdDocente() { return this.id_docente; }
    public void setIdDocente(int idDoc) { this.id_docente = idDoc; }

    public int getIdAula() { return this.id_aula; }
    public void setIdAula(int idAul) { this.id_aula = idAul; }
}