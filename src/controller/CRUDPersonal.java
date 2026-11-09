package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;

import model.ConexionMySQL;
import model.Personal;

public class CRUDPersonal extends ConexionMySQL implements IPersonal {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    public CRUDPersonal() {}
    
    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();

            this.stm = cnx.createStatement();

            this.rs = stm.executeQuery("call sp_listar_personal_completo();");
        } catch (SQLException e) {

        }

        return this.rs;
    }
    
    @Override
    public ResultSet getBuscar(String dni) {
        try {
            this.cnx = this.getConectar();
            
            String cad_sql = "call sp_buscar_personal(?);";

            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, dni);

            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {

        }

        return rs;
    }

    @Override
    public void Registrar(Personal personal) {
        try {
            this.cnx = this.getConectar();
            
            String cad_sql = "call sp_registrar_personal(?, ?, ?, ?, ?, ?, ?, ?);";

            this.pstm = cnx.prepareStatement(cad_sql);
            
            this.pstm.setString(1, personal.getDNI());
            this.pstm.setString(2, personal.getApPaterno());
            this.pstm.setString(3, personal.getApMaterno());
            this.pstm.setString(4, personal.getNombre());
            this.pstm.setString(5, Character.toString(personal.getGenero()));
            this.pstm.setDate(6, (Date) personal.getFechaNacimiento());
            this.pstm.setFloat(7, personal.getSueldo());
            this.pstm.setString(8, personal.getCodigoCargo());

            this.pstm.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void Editar(Personal personal) {
        try {
            this.cnx = this.getConectar();
            
            String cad_sql = "update tb_personal set ap_paterno = ?, ap_materno = ?, nombre = ?, genero = ?, fecha_nacimiento = ?, sueldo = ?, codigo_cargo = ? where dni = ?";

            this.pstm = cnx.prepareStatement(cad_sql);
            
            this.pstm.setString(1, personal.getApPaterno());
            this.pstm.setString(2, personal.getApMaterno());
            this.pstm.setString(3, personal.getNombre());
            this.pstm.setString(4, Character.toString(personal.getGenero()));
            this.pstm.setDate(5, (Date) personal.getFechaNacimiento());
            this.pstm.setFloat(6, personal.getSueldo());
            this.pstm.setString(7, personal.getCodigoCargo());
            this.pstm.setString(8, personal.getDNI());

            this.pstm.executeUpdate();
        } catch (SQLException e) {

        }
    }

    @Override
    public void Borrar(String dni) {
        try {
            this.cnx = this.getConectar();
            
            String cad_sql = "delete from tb_personal where dni = ?";

            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, dni);
            
            this.pstm.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
