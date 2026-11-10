package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IUsuario;
import model.Usuario;
import model.ConexionMySQL;

public class CRUDUsuario extends ConexionMySQL implements IUsuario {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_usuarios();");
        } catch (SQLException e) {
            System.out.println("Error getListar usuario: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_buscar_usuario_id(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar usuario: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Usuario usuario) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_crear_usuario(?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, usuario.getDNI());
            this.pstm.setString(2, usuario.getNombres());
            this.pstm.setString(3, usuario.getApellidos());
            this.pstm.setString(4, usuario.getCorreo());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar usuario: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Usuario usuario) {
        EditarPorId(usuario);
    }

    @Override
    public void EditarPorId(Usuario usuario) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_actualizar_usuario(?, ?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, usuario.getIdUsuario());
            this.pstm.setString(2, usuario.getDNI());
            this.pstm.setString(3, usuario.getNombres());
            this.pstm.setString(4, usuario.getApellidos());
            this.pstm.setString(5, usuario.getCorreo());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar usuario: " + e.getMessage());
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
            String cad_sql = "delete from tb_usuario where id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar usuario: " + e.getMessage());
        }
    }
}