package model;

public class Cargo {
    private String codigo_cargo;
    private String nombre_cargo;
    
    public Cargo() {}
    
    public Cargo(String cc, String nc) {
        this.codigo_cargo = cc;
        this.nombre_cargo = nc;
    }

    public String getCodigoCargo() { return codigo_cargo; }
    public void setCodigoCargo(String cc) { this.codigo_cargo = cc; }

    public String getCargo() { return this.nombre_cargo; }
    public void setCargo(String nc) { this.nombre_cargo = nc; }
}
