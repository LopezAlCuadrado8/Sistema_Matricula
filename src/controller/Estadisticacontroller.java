package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import model.ConexionMySQL;

/**
 * VALOR AGREGADO: panel de estadisticas.
 * Cada metodo devuelve pares (etiqueta -> valor) listos para graficar.
 * Solo lee datos de las tablas existentes; no modifica nada.
 */
public class EstadisticaController {

    // Matriculas activas agrupadas por la carrera del estudiante.
    private static final String SQL_MATRICULADOS_CARRERA =
            "SELECT ca.nombre, COUNT(m.id_matricula) AS total "
            + "FROM tb_carrera ca "
            + "LEFT JOIN tb_estudiante e ON e.id_carrera = ca.id_carrera "
            + "LEFT JOIN tb_matricula m ON m.id_estudiante = e.id_usuario "
            + "AND m.estado = 1 "
            + "GROUP BY ca.id_carrera, ca.nombre "
            + "ORDER BY total DESC, ca.nombre";

    // Matriculas activas agrupadas por bloque, en orden cronologico.
    private static final String SQL_MATRICULADOS_BLOQUE =
            "SELECT b.nombre, COUNT(m.id_matricula) AS total "
            + "FROM tb_bloque b "
            + "LEFT JOIN tb_matricula m ON m.id_bloque = b.id_bloque "
            + "AND m.estado = 1 "
            + "GROUP BY b.id_bloque, b.nombre, b.fecha_inicio "
            + "ORDER BY b.fecha_inicio";

    // Suma de pagos registrados por bloque.
    private static final String SQL_INGRESOS_BLOQUE =
            "SELECT b.nombre, COALESCE(SUM(p.monto), 0) AS total "
            + "FROM tb_bloque b "
            + "LEFT JOIN tb_matricula m ON m.id_bloque = b.id_bloque "
            + "LEFT JOIN tb_pago p ON p.id_matricula = m.id_matricula "
            + "GROUP BY b.id_bloque, b.nombre, b.fecha_inicio "
            + "ORDER BY b.fecha_inicio";

    // Horas semanales asignadas a cada aula segun los horarios.
    private static final String SQL_HORAS_AULA =
            "SELECT a.nombre, "
            + "COALESCE(SUM(TIME_TO_SEC(h.hora_fin) - TIME_TO_SEC(h.hora_inicio)), 0) / 3600 AS horas "
            + "FROM tb_aula a "
            + "LEFT JOIN tb_horario h ON h.id_aula = a.id_aula "
            + "GROUP BY a.id_aula, a.nombre "
            + "ORDER BY horas DESC, a.nombre";

    public Map<String, Double> matriculadosPorCarrera() throws SQLException {
        return consultar(SQL_MATRICULADOS_CARRERA);
    }

    public Map<String, Double> matriculadosPorBloque() throws SQLException {
        return consultar(SQL_MATRICULADOS_BLOQUE);
    }

    public Map<String, Double> ingresosPorBloque() throws SQLException {
        return consultar(SQL_INGRESOS_BLOQUE);
    }

    public Map<String, Double> horasPorAula() throws SQLException {
        return consultar(SQL_HORAS_AULA);
    }

    private Map<String, Double> consultar(String sql) throws SQLException {
        Map<String, Double> datos = new LinkedHashMap<>();

        try (Connection cn = new ConexionMySQL().getConectar()) {

            if (cn == null) {
                throw new SQLException("No se pudo conectar con MySQL.");
            }

            try (Statement st = cn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    datos.put(rs.getString(1), rs.getDouble(2));
                }
            }
        }
        return datos;
    }
}