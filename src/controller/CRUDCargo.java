package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import model.ConexionMySQL;

public class CRUDCargo extends ConexionMySQL implements ICargo {
    private Connection cnx = null;
    private Statement stm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();

            this.stm = cnx.createStatement();

            this.rs = stm.executeQuery("select * from tb_cargo order by cargo asc;");
        } catch (SQLException e) {

        }

        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
