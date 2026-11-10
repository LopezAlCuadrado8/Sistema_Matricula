package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.ICarrera;
import model.Carrera;
import model.ConexionMySQL;

public class CRUDCarrera extends ConexionMySQL implements ICarrera {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_carreras();");
        } catch (SQLException e) {
            System.out.println("Error getListar carrera: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        // SE IMPLEMENTA BÚSQUEDA POR ID USANDO STRING CONVERTIDO A INT
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select id_carrera, nombre, duracion_ciclos from tb_carrera where id_carrera = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar carrera: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Carrera carrera) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_carrera (nombre, duracion_ciclos) values (?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, carrera.getNombre());
            this.pstm.setInt(2, carrera.getDuracionCiclos());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar carrera: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Carrera carrera) {
        EditarPorId(carrera);
    }

    @Override
    public void EditarPorId(Carrera carrera) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_carrera set nombre = ?, duracion_ciclos = ? where id_carrera = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, carrera.getNombre());
            this.pstm.setInt(2, carrera.getDuracionCiclos());
            this.pstm.setInt(3, carrera.getIdCarrera());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar carrera: " + e.getMessage());
        }
    }

    @Override
    public void Borrar(String codigo) {
        BorrarPorId(Integer.parseInt(codigo));
    }

    @Override
    public void BorrarPorId(int id) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "delete from tb_carrera where id_carrera = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar carrera: " + e.getMessage());
        }
    }
}