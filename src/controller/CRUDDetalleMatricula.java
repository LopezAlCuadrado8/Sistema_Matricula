package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IDetalleMatricula;
import model.DetalleMatricula;
import model.ConexionMySQL;

public class CRUDDetalleMatricula extends ConexionMySQL implements IDetalleMatricula {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        // SE LISTA EL DETALLE COMPLETO CON JOIN
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select d.id_matricula, d.id_curso, c.nombre as curso, d.nota, d.estado from tb_detalle_matricula d inner join tb_curso c on d.id_curso = c.id_curso order by d.id_matricula asc;");
        } catch (SQLException e) {
            System.out.println("Error getListar detalle: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR DETALLE POR MATRÍCULA
    public ResultSet getListarPorMatricula(int idMatricula) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_mostrar_detalle_matricula(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, idMatricula);
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getListarPorMatricula: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        // SE BUSCA POR id_matricula|id_curso
        try {
            this.cnx = this.getConectar();
            String[] partes = codigo.split("\\|");
            String cad_sql = "select id_matricula, id_curso, nota, estado from tb_detalle_matricula where id_matricula = ? and id_curso = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(partes[0]));
            this.pstm.setInt(2, Integer.parseInt(partes[1]));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar detalle: " + e.getMessage());
        }
        return this.rs;
    }

    // SE AGREGA MÉTODO PARA LISTAR MATRÍCULAS (PARA EL COMBO)
    public ResultSet getListarMatriculas() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("select m.id_matricula, concat(m.id_matricula, ' - ', u.nombres, ' ', u.apellidos) as descripcion from tb_matricula m inner join tb_estudiante e on m.id_estudiante = e.id_usuario inner join tb_usuario u on e.id_usuario = u.id_usuario where m.estado = 1 order by m.id_matricula asc;");
        } catch (SQLException e) {
            System.out.println("Error getListarMatriculas: " + e.getMessage());
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

    @Override
    public void Registrar(DetalleMatricula detalle) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_agregar_curso(?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, detalle.getIdMatricula());
            this.pstm.setInt(2, detalle.getIdCurso());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar detalle: " + e.getMessage());
        }
    }

    @Override
    public void Editar(DetalleMatricula detalle) {
        EditarPorId(detalle);
    }

    @Override
    public void EditarPorId(DetalleMatricula detalle) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "update tb_detalle_matricula set nota = ?, estado = ? where id_matricula = ? and id_curso = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            if (detalle.getNota() == null) {
                this.pstm.setNull(1, java.sql.Types.DECIMAL);
            } else {
                this.pstm.setDouble(1, detalle.getNota());
            }
            this.pstm.setInt(2, detalle.getEstado());
            this.pstm.setInt(3, detalle.getIdMatricula());
            this.pstm.setInt(4, detalle.getIdCurso());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar detalle: " + e.getMessage());
        }
    }

    @Override
    public void Borrar(String codigo) {
        // SE BORRA FÍSICAMENTE EL DETALLE
        try {
            this.cnx = this.getConectar();
            String[] partes = codigo.split("\\|");
            String cad_sql = "delete from tb_detalle_matricula where id_matricula = ? and id_curso = ?;";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(partes[0]));
            this.pstm.setInt(2, Integer.parseInt(partes[1]));
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar detalle: " + e.getMessage());
        }
    }

    @Override
    public void Retirar(int idMatricula, int idCurso) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_retirar_curso(?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, idMatricula);
            this.pstm.setInt(2, idCurso);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Retirar curso: " + e.getMessage());
        }
    }
}