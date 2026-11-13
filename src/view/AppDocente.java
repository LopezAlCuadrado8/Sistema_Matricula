package view;

import java.sql.ResultSetMetaData;
import model.Docente;

import controller.ValidacionController;
import controller.ValidationDocumentFilter;
import javax.swing.text.AbstractDocument;
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

import controller.CRUDDocente;

public class AppDocente extends JInternalFrame implements ActionListener {
    private static AppDocente instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3;
    private JTextField txt_id, txt_especialidad, txt_grado;
    private JComboBox cbo_usuario;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_docente;
    private JScrollPane scr_docente;
    

    private final CRUDDocente crud_docente = new CRUDDocente();
    private ArrayList<String[]> arr_usuario;
    private final ValidacionController validacion =
            new ValidacionController();

    public AppDocente() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("Instituto San José - DOCENTE");
        this.setSize(500, 520);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("DOCENTES");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Usuario");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_usuario = new JComboBox();
        cbo_usuario.setBounds(130, 60, 340, 25);

        arr_usuario = this.ObtenerUsuarios();
        for (String[] u : arr_usuario)
            cbo_usuario.addItem(u[1]);

        lbl_2 = new JLabel("Especialidad");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_especialidad = new JTextField();
        txt_especialidad.setBounds(130, 100, 340, 25);

        lbl_3 = new JLabel("Grado Académico");
        lbl_3.setBounds(20, 140, 100, 25);
        txt_grado = new JTextField();
        txt_grado.setBounds(130, 140, 340, 25);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 180, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(130, 180, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(250, 180, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(370, 180, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(150, 430, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_docente = new JTable();
        tb_docente.setRowHeight(20);
        tb_docente.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_docente = new JScrollPane(tb_docente);
        scr_docente.setBounds(10, 220, 470, 170);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_usuario);
        this.add(lbl_2);
        this.add(txt_especialidad);
        this.add(lbl_3);
        this.add(txt_grado);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_docente);

     // Especialidad: máximo 50 caracteres.
        ((AbstractDocument) txt_especialidad.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("ALFANUMERICO", 50)
        );

        // Grado académico: máximo 30 caracteres.
        ((AbstractDocument) txt_grado.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("ALFANUMERICO", 30)
        );

        ControladorClick click = new ControladorClick();
        tb_docente.addMouseListener(click);
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
        try (ResultSet rs = crud_docente.getListarUsuarios()) {
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

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_docente.getListar()) {
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
            System.err.println("Error para cargar docentes: " + e.getMessage());
        }

        tb_docente.setModel(modelo);
        tb_docente.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_docente.getColumnModel();
        if (columnas.getColumnCount() >= 5) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
        }
    }

    

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_docente.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_docente.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_docente.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_usuario")));
                txt_especialidad.setText(rs.getString("especialidad"));
                txt_grado.setText(rs.getString("grado_academico"));

                cbo_usuario.setEnabled(false);
                for (int i = 0; i < arr_usuario.size(); i++) {
                    if (arr_usuario.get(i)[0].equals(id)) {
                        cbo_usuario.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar docente: " + ex.getMessage());
            }
        }
    }
    
    private boolean ValidarFormulario() {

        String especialidad = txt_especialidad.getText().trim();
        String grado = txt_grado.getText().trim();

        if (cbo_usuario.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
                    this, "Seleccione un usuario.");
            cbo_usuario.requestFocus();
            return false;
        }

        if (validacion.estaVacio(especialidad)) {
            JOptionPane.showMessageDialog(
                    this, "Ingrese la especialidad.");
            txt_especialidad.requestFocus();
            return false;
        }

        if (!validacion.validarLongitud(especialidad, 1, 50)) {
            JOptionPane.showMessageDialog(
                    this, "La especialidad debe tener máximo 50 caracteres.");
            txt_especialidad.requestFocus();
            return false;
        }

        if (validacion.estaVacio(grado)) {
            JOptionPane.showMessageDialog(
                    this, "Ingrese el grado académico.");
            txt_grado.requestFocus();
            return false;
        }

        if (!validacion.validarLongitud(grado, 1, 30)) {
            JOptionPane.showMessageDialog(
                    this, "El grado académico debe tener máximo 30 caracteres.");
            txt_grado.requestFocus();
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

        if (e.getSource() == btn_borrar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Seleccione un docente de la tabla.");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de borrar el registro?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {
                crud_docente.BorrarPorId(
                        Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(
                        this, "Docente eliminado.");
            }

            MostrarDatos();
            LimpiarDatos();
            return;
        }

        if (e.getSource() != btn_agregar
                && e.getSource() != btn_editar) {
            return;
        }

        if (!ValidarFormulario()) {
            return;
        }

        Docente docente = new Docente();

        String nomUsu = cbo_usuario.getSelectedItem().toString();

        for (String[] u : arr_usuario) {
            if (u[1].equals(nomUsu)) {
                docente.setIdUsuario(Integer.parseInt(u[0]));
                break;
            }
        }

        docente.setEspecialidad(
                txt_especialidad.getText().trim()
        );

        docente.setGradoAcademico(
                txt_grado.getText().trim()
        );

        if (e.getSource() == btn_agregar) {

            crud_docente.Registrar(docente);

            JOptionPane.showMessageDialog(
                    this, "Docente registrado.");

        } else if (e.getSource() == btn_editar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Seleccione un docente de la tabla.");
                return;
            }

            docente.setIdUsuario(
                    Integer.parseInt(txt_id.getText())
            );

            crud_docente.EditarPorId(docente);

            JOptionPane.showMessageDialog(
                    this, "Docente actualizado.");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        cbo_usuario.setEnabled(true);
        tb_docente.clearSelection();
        txt_id.setText("");
        txt_especialidad.setText("");
        txt_grado.setText("");
        cbo_usuario.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_especialidad.requestFocus();
    }

    public static AppDocente getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppDocente();
        }
        return instancia;
    }
}