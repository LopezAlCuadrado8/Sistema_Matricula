package view;

import java.sql.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;
import model.Matricula;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import controller.CRUDMatricula;

public class AppMatricula extends JInternalFrame implements ActionListener {
    private static AppMatricula instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3;
    private JTextField txt_id;
    private JDateChooser dc_fecha;
    private JComboBox cbo_estudiante, cbo_bloque;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_anular, btn_borrar, btn_cerrar;
    private JTable tb_matricula;
    private JScrollPane scr_matricula;

    private final CRUDMatricula crud_matricula = new CRUDMatricula();
    private ArrayList<String[]> arr_estudiante;
    private ArrayList<String[]> arr_bloque;

    public AppMatricula() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - MATRÍCULA");
        this.setSize(560, 540);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("MATRÍCULAS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Estudiante");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_estudiante = new JComboBox();
        cbo_estudiante.setBounds(130, 60, 400, 25);

        arr_estudiante = this.ObtenerEstudiantes();
        for (String[] u : arr_estudiante)
            cbo_estudiante.addItem(u[1]);

        lbl_2 = new JLabel("Bloque");
        lbl_2.setBounds(20, 100, 100, 25);
        cbo_bloque = new JComboBox();
        cbo_bloque.setBounds(130, 100, 250, 25);

        arr_bloque = this.ObtenerBloques();
        for (String[] b : arr_bloque)
            cbo_bloque.addItem(b[1]);

        lbl_3 = new JLabel("Fecha");
        lbl_3.setBounds(20, 140, 100, 25);
        dc_fecha = new JDateChooser();
        dc_fecha.setDateFormatString("dd/MM/yyyy");
        dc_fecha.setBounds(130, 140, 140, 25);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 180, 80, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(100, 180, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(200, 180, 80, 25);
        btn_editar.addActionListener(this);

        btn_anular = new JButton("ANULAR");
        btn_anular.setBounds(290, 180, 80, 25);
        btn_anular.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(380, 180, 80, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(180, 450, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_matricula = new JTable();
        tb_matricula.setRowHeight(20);
        tb_matricula.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_matricula = new JScrollPane(tb_matricula);
        scr_matricula.setBounds(10, 220, 530, 210);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_estudiante);
        this.add(lbl_2);
        this.add(cbo_bloque);
        this.add(lbl_3);
        this.add(dc_fecha);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_anular);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_matricula);

        ControladorClick click = new ControladorClick();
        tb_matricula.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean anular, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_anular.setEnabled(anular);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerEstudiantes() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione estudiante"});
        try (ResultSet rs = crud_matricula.getListarEstudiantes()) {
            while (rs.next()) {
                arr_lista.add(new String[]{
                    rs.getString("id_usuario"),
                    rs.getString("codigo") + " - " + rs.getString("nombres") + " " + rs.getString("apellidos")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estudiantes: " + e.getMessage());
        }
        return arr_lista;
    }

    private ArrayList<String[]> ObtenerBloques() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione bloque"});
        try (ResultSet rs = crud_matricula.getListarBloques()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_bloque"), rs.getString("nombre") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener bloques: " + e.getMessage());
        }
        return arr_lista;
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_matricula.getListar()) {
        	ResultSetMetaData meta = rs.getMetaData();
            int nc = meta.getColumnCount();
            for (int i = 1; i <= nc; i++)
                modelo.addColumn(meta.getColumnLabel(i));
            while (rs.next()) {
                Object[] arr_fila = new Object[nc];
                for (int i = 0; i < nc; i++)
                    arr_fila[i] = rs.getObject(i + 1);
                modelo.addRow(arr_fila);
            }
        } catch (SQLException e) {
            System.err.println("Error para cargar matrículas: " + e.getMessage());
        }

        tb_matricula.setModel(modelo);
        tb_matricula.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_matricula.getColumnModel();
        if (columnas.getColumnCount() >= 6) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(1).setCellRenderer(centro);
            columnas.getColumn(5).setCellRenderer(centro);
        }
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_matricula.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR, ANULAR Y BORRAR; SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true, true);

            String id = tb_matricula.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_matricula.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_matricula")));
                dc_fecha.setDate(rs.getDate("fecha"));

                int idEst = rs.getInt("id_estudiante");
                for (int i = 0; i < arr_estudiante.size(); i++) {
                    if (arr_estudiante.get(i)[0].equals(String.valueOf(idEst))) {
                        cbo_estudiante.setSelectedIndex(i);
                        break;
                    }
                }

                int idBlo = rs.getInt("id_bloque");
                for (int i = 0; i < arr_bloque.size(); i++) {
                    if (arr_bloque.get(i)[0].equals(String.valueOf(idBlo))) {
                        cbo_bloque.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar matrícula: " + ex.getMessage());
            }
        }
    }
    
    private boolean ValidarFormulario() {

        if (cbo_estudiante.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un estudiante");
            cbo_estudiante.requestFocus();
            return false;
        }

        if (cbo_bloque.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un bloque");
            cbo_bloque.requestFocus();
            return false;
        }

        if (dc_fecha.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Seleccione la fecha de matrícula");
            dc_fecha.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_cerrar) {
            dispose();
            return;
        }

        if (e.getSource() == btn_nuevo) {
            LimpiarDatos();
            return;
        }

        // ANULAR MATRÍCULA
        if (e.getSource() == btn_anular) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una matrícula de la tabla");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de anular la matrícula?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {

                crud_matricula.Anular(
                        Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(this, "Matrícula anulada");

                MostrarDatos();
                LimpiarDatos();
            }

            return;
        }

        // BORRAR MATRÍCULA
        if (e.getSource() == btn_borrar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una matrícula de la tabla");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de borrar el registro?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {

                crud_matricula.BorrarPorId(
                        Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(this, "Matrícula eliminada");

                MostrarDatos();
                LimpiarDatos();
            }

            return;
        }

        // SOLO CONTINUAR SI ES AGREGAR O EDITAR
        if (e.getSource() != btn_agregar &&
                e.getSource() != btn_editar) {
            return;
        }

        if (e.getSource() == btn_editar && txt_id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una matrícula de la tabla");
            return;
        }

        if (!ValidarFormulario()) {
            return;
        }

        Matricula matricula = new Matricula();

        matricula.setFecha(
                new java.sql.Date(dc_fecha.getDate().getTime())
        );

        // Obtener ID del estudiante seleccionado
        String[] estudiante =
                arr_estudiante.get(cbo_estudiante.getSelectedIndex());

        matricula.setIdEstudiante(
                Integer.parseInt(estudiante[0])
        );

        // Obtener ID del bloque seleccionado
        String[] bloque =
                arr_bloque.get(cbo_bloque.getSelectedIndex());

        matricula.setIdBloque(
                Integer.parseInt(bloque[0])
        );

        matricula.setEstado(1);

        if (e.getSource() == btn_agregar) {

            crud_matricula.Registrar(matricula);

            JOptionPane.showMessageDialog(this, "Matrícula registrada");

        } else if (e.getSource() == btn_editar) {

            matricula.setIdMatricula(
                    Integer.parseInt(txt_id.getText())
            );

            crud_matricula.EditarPorId(matricula);

            JOptionPane.showMessageDialog(this, "Matrícula actualizada");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        tb_matricula.clearSelection();
        txt_id.setText("");
        dc_fecha.setDate(null);
        cbo_estudiante.setSelectedIndex(0);
        cbo_bloque.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false, false);
    }

    public static AppMatricula getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppMatricula();
        }
        return instancia;
    }
}