package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IHorario;
import model.Horario;
import model.ConexionMySQL;

public class CRUDHorario extends ConexionMySQL implements IHorario {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_horarios();");
        } catch (SQLException e) {
            System.out.println("Error getListar horario: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "select id_horario, dia, hora_inicio, hora_fin, id_curso, id_docente, id_aula from tb_horario where id_horario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar horario: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR CURSOS (PARA EL COMBO)
    public ResultSet getListarCursos() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_curso, nombre from tb_curso order by nombre asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarCursos: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR DOCENTES (PARA EL COMBO)
    public ResultSet getListarDocentes() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select d.id_usuario, concat(u.nombres, ' ', u.apellidos) as docente from tb_docente d inner join tb_usuario u on d.id_usuario = u.id_usuario order by u.apellidos asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarDocentes: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR AULAS (PARA EL COMBO)
    public ResultSet getListarAulas() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select id_aula, nombre from tb_aula order by nombre asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarAulas: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public void Registrar(Horario horario) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "insert into tb_horario (dia, hora_inicio, hora_fin, id_curso, id_docente, id_aula) values (?, ?, ?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, horario.getDia());
            this.pstm.setTime(2, horario.getHoraInicio());
            this.pstm.setTime(3, horario.getHoraFin());
            this.pstm.setInt(4, horario.getIdCurso());
            this.pstm.setInt(5, horario.getIdDocente());
            this.pstm.setInt(6, horario.getIdAula());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar horario: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Horario horario) {
        EditarPorId(horario);
    }

    @Override
    public void EditarPorId(Horario horario) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_horario set dia = ?, hora_inicio = ?, hora_fin = ?, id_curso = ?, id_docente = ?, id_aula = ? where id_horario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setString(1, horario.getDia());
            this.pstm.setTime(2, horario.getHoraInicio());
            this.pstm.setTime(3, horario.getHoraFin());
            this.pstm.setInt(4, horario.getIdCurso());
            this.pstm.setInt(5, horario.getIdDocente());
            this.pstm.setInt(6, horario.getIdAula());
            this.pstm.setInt(7, horario.getIdHorario());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar horario: " + e.getMessage());
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
            String cad_sql = "delete from tb_horario where id_horario = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar horario: " + e.getMessage());
        }
    }
}