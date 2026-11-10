package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IMatricula;
import model.Matricula;
import model.ConexionMySQL;

public class CRUDMatricula extends ConexionMySQL implements IMatricula {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_matriculas();");
        } catch (SQLException e) {
            System.out.println("Error getListar matricula: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select id_matricula, fecha, estado, id_estudiante, id_bloque from tb_matricula where id_matricula = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar matricula: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR ESTUDIANTES (PARA EL COMBO)
    public ResultSet getListarEstudiantes() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select e.id_usuario, e.codigo, u.nombres, u.apellidos from tb_estudiante e inner join tb_usuario u on e.id_usuario = u.id_usuario order by u.apellidos asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarEstudiantes: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR BLOQUES (PARA EL COMBO)
    public ResultSet getListarBloques() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_bloque, nombre from tb_bloque order by id_bloque asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarBloques: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Matricula matricula) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_crear_matricula(?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setDate(1, matricula.getFecha());
            this.pstm.setInt(2, matricula.getIdEstudiante());
            this.pstm.setInt(3, matricula.getIdBloque());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar matricula: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Matricula matricula) {
        EditarPorId(matricula);
    }

    @Override
    public void EditarPorId(Matricula matricula) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_matricula set fecha = ?, estado = ?, id_estudiante = ?, id_bloque = ? where id_matricula = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setDate(1, matricula.getFecha());
            this.pstm.setInt(2, matricula.getEstado());
            this.pstm.setInt(3, matricula.getIdEstudiante());
            this.pstm.setInt(4, matricula.getIdBloque());
            this.pstm.setInt(5, matricula.getIdMatricula());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar matricula: " + e.getMessage());
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
            String cad_sql = "call sp_eliminar_matricula(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar matricula: " + e.getMessage());
        }
    }

    @Override
    public void Anular(int id) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_anular_matricula(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Anular matricula: " + e.getMessage());
        }
    }
}