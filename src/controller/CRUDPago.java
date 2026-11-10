package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import interfaces.IPago;
import model.Pago;
import model.ConexionMySQL;

public class CRUDPago extends ConexionMySQL implements IPago {
    private Connection cnx = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet rs = null;

    @Override
    public ResultSet getListar() {
        try {
            this.cnx = this.getConectar();
            this.stm = cnx.createStatement();
            this.rs = stm.executeQuery("call sp_mostrar_pagos();");
        } catch (SQLException e) {
            System.out.println("Error getListar pago: " + e.getMessage());
        }
        return this.rs;
    }

    @Override
    public ResultSet getBuscar(String codigo) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_buscar_pago_id(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, Integer.parseInt(codigo));
            this.rs = this.pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error getBuscar pago: " + e.getMessage());
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

    @Override
    public void Registrar(Pago pago) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_crear_pago(?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setDouble(1, pago.getMonto());
            this.pstm.setDate(2, pago.getFecha());
            this.pstm.setString(3, pago.getMetodo());
            this.pstm.setInt(4, pago.getIdMatricula());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Registrar pago: " + e.getMessage());
        }
    }

    @Override
    public void Editar(Pago pago) {
        EditarPorId(pago);
    }

    @Override
    public void EditarPorId(Pago pago) {
        try {
            this.cnx = this.getConectar();
            String cad_sql = "call sp_actualizar_pago(?, ?, ?, ?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, pago.getIdPago());
            this.pstm.setDouble(2, pago.getMonto());
            this.pstm.setDate(3, pago.getFecha());
            this.pstm.setString(4, pago.getMetodo());
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Editar pago: " + e.getMessage());
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
            String cad_sql = "call sp_eliminar_pago(?);";
            this.pstm = cnx.prepareStatement(cad_sql);
            this.pstm.setInt(1, id);
            this.pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error Borrar pago: " + e.getMessage());
        }
    }
}