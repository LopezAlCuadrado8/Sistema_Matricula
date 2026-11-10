package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IAula;
import model.Aula;
import model.ConexionMySQL;

public class CRUDAula extends ConexionMySQL implements IAula {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_aula, nombre, capacidad from tb_aula order by id_aula asc;");
        } catch (SQLException e) {
            System.out.println("Error getListar aula: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select id_aula, nombre, capacidad from tb_aula where id_aula = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar aula: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Aula aula) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_aula (nombre, capacidad) values (?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, aula.getNombre());
            this.pstm.setInt(2, aula.getCapacidad());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar aula: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Aula aula) {
        EditarPorId(aula);
    }

    @Override
    public void EditarPorId(Aula aula) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_aula set nombre = ?, capacidad = ? where id_aula = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, aula.getNombre());
            this.pstm.setInt(2, aula.getCapacidad());
            this.pstm.setInt(3, aula.getIdAula());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar aula: " + e.getMessage());
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
            String cad_sql = "delete from tb_aula where id_aula = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar aula: " + e.getMessage());
        }
    }
}