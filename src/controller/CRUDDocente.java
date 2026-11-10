package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IDocente;
import model.Docente;
import model.ConexionMySQL;

public class CRUDDocente extends ConexionMySQL implements IDocente {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_docentes();");
        } catch (SQLException e) {
            System.out.println("Error getListar docente: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select d.id_usuario, d.especialidad, d.grado_academico, u.nombres, u.apellidos from tb_docente d inner join tb_usuario u on d.id_usuario = u.id_usuario where d.id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar docente: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR USUARIOS (PARA EL COMBO)
    public ResultSet getListarUsuarios() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_usuario, dni, nombres, apellidos from tb_usuario order by apellidos asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarUsuarios: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Docente docente) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_docente (id_usuario, especialidad, grado_academico) values (?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, docente.getIdUsuario());
            this.pstm.setString(2, docente.getEspecialidad());
            this.pstm.setString(3, docente.getGradoAcademico());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar docente: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Docente docente) {
        EditarPorId(docente);
    }

    @Override
    public void EditarPorId(Docente docente) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_docente set especialidad = ?, grado_academico = ? where id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, docente.getEspecialidad());
            this.pstm.setString(2, docente.getGradoAcademico());
            this.pstm.setInt(3, docente.getIdUsuario());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar docente: " + e.getMessage());
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
            String cad_sql = "delete from tb_docente where id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar docente: " + e.getMessage());
        }
    }
}