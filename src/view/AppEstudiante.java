package view;

import com.mysql.jdbc.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;
import model.Estudiante;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import controller.CRUDEstudiante;

public class AppEstudiante extends JInternalFrame implements ActionListener {
    private static AppEstudiante instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4;
    private JTextField txt_id, txt_codigo;
    private JDateChooser dc_fecha;
    private JComboBox cbo_usuario, cbo_carrera;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_estudiante;
    private JScrollPane scr_estudiante;

    private final CRUDEstudiante crud_estudiante = new CRUDEstudiante();
    private ArrayList<String[]> arr_usuario;
    private ArrayList<String[]> arr_carrera;

    public AppEstudiante() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - ESTUDIANTE");
        this.setSize(520, 540);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("ESTUDIANTES");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Usuario");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_usuario = new JComboBox();
        cbo_usuario.setBounds(130, 60, 360, 25);

        arr_usuario = this.ObtenerUsuarios();
        for (String[] u : arr_usuario)
            cbo_usuario.addItem(u[1]);

        lbl_2 = new JLabel("Código");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_codigo = new JTextField();
        txt_codigo.setBounds(130, 100, 120, 25);

        lbl_3 = new JLabel("Fecha Ingreso");
        lbl_3.setBounds(20, 140, 100, 25);
        dc_fecha = new JDateChooser();
        dc_fecha.setDateFormatString("dd/MM/yyyy");
        dc_fecha.setBounds(130, 140, 120, 25);

        lbl_4 = new JLabel("Carrera");
        lbl_4.setBounds(20, 180, 100, 25);
        cbo_carrera = new JComboBox();
        cbo_carrera.setBounds(130, 180, 360, 25);

        arr_carrera = this.ObtenerCarreras();
        for (String[] c : arr_carrera)
            cbo_carrera.addItem(c[1]);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 220, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(130, 220, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(250, 220, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(370, 220, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(160, 450, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_estudiante = new JTable();
        tb_estudiante.setRowHeight(20);
        tb_estudiante.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_estudiante = new JScrollPane(tb_estudiante);
        scr_estudiante.setBounds(10, 260, 490, 180);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_usuario);
        this.add(lbl_2);
        this.add(txt_codigo);
        this.add(lbl_3);
        this.add(dc_fecha);
        this.add(lbl_4);
        this.add(cbo_carrera);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_estudiante);

        ControladorTxt ctxt = new ControladorTxt();
        txt_codigo.addKeyListener(ctxt);

        ControladorClick click = new ControladorClick();
        tb_estudiante.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerUsuarios() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione usuario"});
        try (ResultSet rs = crud_estudiante.getListarUsuarios()) {
            while (rs.next()) {
                arr_lista.add(new String[]{
                    rs.getString("id_usuario"),
                    rs.getString("dni") + " - " + rs.getString("nombres") + " " + rs.getString("apellidos")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios: " + e.getMessage());
        }
        return arr_lista;
    }

    private ArrayList<String[]> ObtenerCarreras() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione carrera"});
        try (ResultSet rs = crud_estudiante.getListarCarreras()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_carrera"), rs.getString("nombre") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener carreras: " + e.getMessage());
        }
        return arr_lista;
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_estudiante.getListar()) {
            ResultSetMetaData meta = (ResultSetMetaData) rs.getMetaData();
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
            System.err.println("Error para cargar estudiantes: " + e.getMessage());
        }

        tb_estudiante.setModel(modelo);
        tb_estudiante.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_estudiante.getColumnModel();
        if (columnas.getColumnCount() >= 6) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(1).setCellRenderer(centro);
            columnas.getColumn(5).setCellRenderer(centro);
        }
    }

    private class ControladorTxt implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txt_codigo && txt_codigo.getText().length() >= 10) {
                e.consume();
            }
        }
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_estudiante.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_estudiante.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_estudiante.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_usuario")));
                txt_codigo.setText(rs.getString("codigo"));
                dc_fecha.setDate(rs.getDate("fecha_ingreso"));

                int idCar = rs.getInt("id_carrera");
                for (int i = 0; i < arr_carrera.size(); i++) {
                    if (arr_carrera.get(i)[0].equals(String.valueOf(idCar))) {
                        cbo_carrera.setSelectedIndex(i);
                        break;
                    }
                }

                cbo_usuario.setEnabled(false);
                for (int i = 0; i < arr_usuario.size(); i++) {
                    if (arr_usuario.get(i)[0].equals(id)) {
                        cbo_usuario.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar estudiante: " + ex.getMessage());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_cerrar) {
            dispose();
            return;
        }

        if (e.getSource() == btn_nuevo) {
            LimpiarDatos();
        } else {
            if (cbo_usuario.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione usuario");
                cbo_usuario.requestFocus();
                return;
            }
            if (txt_codigo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese código de estudiante");
                txt_codigo.requestFocus();
                return;
            }
            if (dc_fecha.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione fecha de ingreso");
                return;
            }
            if (cbo_carrera.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione carrera");
                cbo_carrera.requestFocus();
                return;
            }

            Estudiante estudiante = new Estudiante();

            String nomUsu = cbo_usuario.getSelectedItem().toString();
            for (String[] u : arr_usuario) {
                if (u[1].equals(nomUsu)) {
                    estudiante.setIdUsuario(Integer.parseInt(u[0]));
                    break;
                }
            }

            estudiante.setCodigo(txt_codigo.getText().trim());
            Date fi = dc_fecha.getDate();
            estudiante.setFechaIngreso(new java.sql.Date(fi.getTime()));

            String nomCar = cbo_carrera.getSelectedItem().toString();
            for (String[] c : arr_carrera) {
                if (c[1].equals(nomCar)) {
                    estudiante.setIdCarrera(Integer.parseInt(c[0]));
                    break;
                }
            }

            if (e.getSource() == btn_agregar) {
                crud_estudiante.Registrar(estudiante);
                JOptionPane.showMessageDialog(null, "Estudiante Registrado");
            } else if (e.getSource() == btn_editar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un estudiante de la tabla");
                    return;
                }
                estudiante.setIdUsuario(Integer.parseInt(txt_id.getText()));
                crud_estudiante.EditarPorId(estudiante);
                JOptionPane.showMessageDialog(null, "Estudiante Actualizado");
            } else if (e.getSource() == btn_borrar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un estudiante de la tabla");
                    return;
                }
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro de borrar el registro?", "SENATI", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    crud_estudiante.BorrarPorId(Integer.parseInt(txt_id.getText()));
                    JOptionPane.showMessageDialog(null, "Estudiante Eliminado");
                }
            }

            MostrarDatos();
            LimpiarDatos();
        }
    }

    private void LimpiarDatos() {
        cbo_usuario.setEnabled(true);
        tb_estudiante.clearSelection();
        txt_id.setText("");
        txt_codigo.setText("");
        dc_fecha.setDate(null);
        cbo_usuario.setSelectedIndex(0);
        cbo_carrera.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_codigo.requestFocus();
    }

    public static AppEstudiante getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppEstudiante();
        }
        return instancia;
    }
}