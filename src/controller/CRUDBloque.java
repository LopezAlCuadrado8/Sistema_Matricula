package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IBloque;
import model.Bloque;
import model.ConexionMySQL;

public class CRUDBloque extends ConexionMySQL implements IBloque {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_bloque, nombre, fecha_inicio, fecha_fin from tb_bloque order by id_bloque asc;");
        } catch (SQLException e) {
            System.out.println("Error getListar bloque: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select id_bloque, nombre, fecha_inicio, fecha_fin from tb_bloque where id_bloque = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar bloque: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Bloque bloque) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_bloque (nombre, fecha_inicio, fecha_fin) values (?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, bloque.getNombre());
            this.pstm.setDate(2, bloque.getFechaInicio());
            this.pstm.setDate(3, bloque.getFechaFin());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar bloque: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Bloque bloque) {
        EditarPorId(bloque);
    }

    @Override
    public void EditarPorId(Bloque bloque) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_bloque set nombre = ?, fecha_inicio = ?, fecha_fin = ? where id_bloque = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, bloque.getNombre());
            this.pstm.setDate(2, bloque.getFechaInicio());
            this.pstm.setDate(3, bloque.getFechaFin());
            this.pstm.setInt(4, bloque.getIdBloque());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar bloque: " + e.getMessage());
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
            String cad_sql = "delete from tb_bloque where id_bloque = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar bloque: " + e.getMessage());
        }
    }
}