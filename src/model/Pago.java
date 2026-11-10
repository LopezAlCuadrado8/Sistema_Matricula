package model;

import java.sql.Date;

public class Pago {
    private int id_pago;
    private double monto;
    private Date fecha;
    private String metodo;
    private int id_matricula;

    public Pago() {}

    public Pago(int id, double monto, Date f, String met, int idMat) {
        this.id_pago = id;
        this.monto = monto;
        this.fecha = f;
        this.metodo = met;
        this.id_matricula = idMat;
    }

    public int getIdPago() { return this.id_pago; }
    public void setIdPago(int id) { this.id_pago = id; }

    public double getMonto() { return this.monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public Date getFecha() { return this.fecha; }
    public void setFecha(Date f) { this.fecha = f; }

    public String getMetodo() { return this.metodo; }
    public void setMetodo(String met) { this.metodo = met; }

    public int getIdMatricula() { return this.id_matricula; }
    public void setIdMatricula(int idMat) { this.id_matricula = idMat; }
}