package model;

import java.sql.Date;

public class Personal {
    private String dni;
    private String ap_paterno;
    private String ap_materno;
    private String nombre;
    private char genero;
    private Date fecha_nacimiento;
    private float sueldo;
    private String codigo_cargo;
    
    public Personal() {}
    
    public Personal(String dni, String app, String apm, String nom, char gen, Date fn, float sue, String cc) {
        this.dni = dni;
        this.ap_paterno = app;
        this.ap_materno = apm;
        this.nombre = nom;
        this.genero = gen;
        this.fecha_nacimiento = fn;
        this.sueldo = sue;
        this.codigo_cargo = cc;
    }
    
    public String getDNI() { return this.dni; }
    public void setDNI(String dni) { this.dni = dni; }

    public String getApPaterno() { return this.ap_paterno; }
    public void setApPaterno(String app) { this.ap_paterno = app; }
    
    public String getApMaterno() { return this.ap_materno; }
    public void setApMaterno(String apm) { this.ap_materno = apm; }
    
    public String getNombre() { return this.nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public char getGenero() { return this.genero; }
    public void setGenero(char genero) { this.genero = genero; }

    public Date getFechaNacimiento() { return this.fecha_nacimiento; }
    public void setFechaNacimiento(Date fn) { this.fecha_nacimiento = fn; }
    
    public float getSueldo() { return this.sueldo; }
    public void setSueldo(float sue) { this.sueldo = sue; }
    
    public String getCodigoCargo() { return this.codigo_cargo; }
    public void setCodigoCargo(String cc) { this.codigo_cargo = cc; }

    public String getNombreCompleto() {
        return (this.nombre + " " + this.ap_paterno + " " + this.ap_materno).toUpperCase();
    }

    public String getMostrarGenero() {
        return this.genero == 'F' ? "Femenino" : "Masculino";
    }
}
