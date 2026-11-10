package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.ICurso;
import model.Curso;
import model.ConexionMySQL;

public class CRUDCurso extends ConexionMySQL implements ICurso {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_cursos();");
        } catch (SQLException e) {
            System.out.println("Error getListar curso: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_buscar_curso_id(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar curso: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR CARRERAS (PARA EL COMBO)
    public ResultSet getListarCarreras() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_carrera, nombre from tb_carrera order by nombre asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarCarreras: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Curso curso) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_crear_curso(?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, curso.getNombre());
            this.pstm.setInt(2, curso.getCreditos());
            this.pstm.setInt(3, curso.getHoras());
            this.pstm.setInt(4, curso.getIdCarrera());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar curso: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Curso curso) {
        EditarPorId(curso);
    }

    @Override
    public void EditarPorId(Curso curso) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_actualizar_curso(?, ?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, curso.getIdCurso());
            this.pstm.setString(2, curso.getNombre());
            this.pstm.setInt(3, curso.getCreditos());
            this.pstm.setInt(4, curso.getHoras());
            this.pstm.setInt(5, curso.getIdCarrera());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar curso: " + e.getMessage());
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
            String cad_sql = "delete from tb_curso where id_curso = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar curso: " + e.getMessage());
        }
    }
}