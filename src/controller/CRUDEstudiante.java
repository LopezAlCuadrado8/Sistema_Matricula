package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IEstudiante;
import model.Estudiante;
import model.ConexionMySQL;

public class CRUDEstudiante extends ConexionMySQL implements IEstudiante {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_estudiantes();");
        } catch (SQLException e) {
            System.out.println("Error getListar estudiante: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        // SE BUSCA POR id_usuario
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select e.id_usuario, e.codigo, e.fecha_ingreso, e.id_carrera, u.nombres, u.apellidos from tb_estudiante e inner join tb_usuario u on e.id_usuario = u.id_usuario where e.id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar estudiante: " + e.getMessage());
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
    public void Registrar(Estudiante estudiante) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_estudiante (id_usuario, codigo, fecha_ingreso, id_carrera) values (?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, estudiante.getIdUsuario());
            this.pstm.setString(2, estudiante.getCodigo());
            this.pstm.setDate(3, estudiante.getFechaIngreso());
            this.pstm.setInt(4, estudiante.getIdCarrera());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar estudiante: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Estudiante estudiante) {
        EditarPorId(estudiante);
    }

    @Override
    public void EditarPorId(Estudiante estudiante) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_estudiante set codigo = ?, fecha_ingreso = ?, id_carrera = ? where id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, estudiante.getCodigo());
            this.pstm.setDate(2, estudiante.getFechaIngreso());
            this.pstm.setInt(3, estudiante.getIdCarrera());
            this.pstm.setInt(4, estudiante.getIdUsuario());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar estudiante: " + e.getMessage());
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
            String cad_sql = "delete from tb_estudiante where id_usuario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar estudiante: " + e.getMessage());
        }
    }
}