package view;

import java.sql.ResultSetMetaData;
import model.DetalleMatricula;

import javax.swing.text.AbstractDocument;
import controller.ValidacionController;
import controller.ValidationDocumentFilter;
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

import controller.CRUDDetalleMatricula;

public class AppDetalleMatricula extends JInternalFrame implements ActionListener {
    private static AppDetalleMatricula instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4;
    private JTextField txt_nota, txt_estado;
    private JComboBox cbo_matricula, cbo_curso;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_retirar, btn_borrar, btn_cerrar;
    private JTable tb_detalle;
    private JScrollPane scr_detalle;

    private final CRUDDetalleMatricula crud_detalle = new CRUDDetalleMatricula();
    private ArrayList<String[]> arr_matricula;
    private ArrayList<String[]> arr_curso;
    private final ValidacionController validacion =
            new ValidacionController();

    public AppDetalleMatricula() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - DETALLE MATRÍCULA");
        this.setSize(560, 540);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("DETALLE MATRÍCULA");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 22));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 350, 25);

        lbl_1 = new JLabel("Matrícula");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_matricula = new JComboBox();
        cbo_matricula.setBounds(130, 60, 400, 25);

        arr_matricula = this.ObtenerMatriculas();
        for (String[] m : arr_matricula)
            cbo_matricula.addItem(m[1]);

        lbl_2 = new JLabel("Curso");
        lbl_2.setBounds(20, 100, 100, 25);
        cbo_curso = new JComboBox();
        cbo_curso.setBounds(130, 100, 400, 25);

        arr_curso = this.ObtenerCursos();
        for (String[] c : arr_curso)
            cbo_curso.addItem(c[1]);

        lbl_3 = new JLabel("Nota");
        lbl_3.setBounds(20, 140, 100, 25);
        txt_nota = new JTextField();
        txt_nota.setHorizontalAlignment(JTextField.CENTER);
        txt_nota.setBounds(130, 140, 80, 25);

        lbl_4 = new JLabel("Estado");
        lbl_4.setBounds(230, 140, 60, 25);
        txt_estado = new JTextField("1");
        txt_estado.setHorizontalAlignment(JTextField.CENTER);
        txt_estado.setBounds(290, 140, 50, 25);
        txt_estado.setEditable(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 180, 80, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(100, 180, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(200, 180, 80, 25);
        btn_editar.addActionListener(this);

        btn_retirar = new JButton("RETIRAR");
        btn_retirar.setBounds(290, 180, 80, 25);
        btn_retirar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(380, 180, 80, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(180, 450, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_detalle = new JTable();
        tb_detalle.setRowHeight(20);
        tb_detalle.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_detalle = new JScrollPane(tb_detalle);
        scr_detalle.setBounds(10, 220, 530, 210);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_matricula);
        this.add(lbl_2);
        this.add(cbo_curso);
        this.add(lbl_3);
        this.add(txt_nota);
        this.add(lbl_4);
        this.add(txt_estado);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_retirar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_detalle);

        ((AbstractDocument) txt_nota.getDocument()).setDocumentFilter(
                new ValidationDocumentFilter("DECIMAL", 5)
        );

        ControladorClick click = new ControladorClick();
        tb_detalle.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean retirar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_retirar.setEnabled(retirar);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerMatriculas() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione matrícula"});
        try (ResultSet rs = crud_detalle.getListarMatriculas()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_matricula"), rs.getString("descripcion") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener matrículas: " + e.getMessage());
        }
        return arr_lista;
    }

    private ArrayList<String[]> ObtenerCursos() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione curso"});
        try (ResultSet rs = crud_detalle.getListarCursos()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_curso"), rs.getString("nombre") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cursos: " + e.getMessage());
        }
        return arr_lista;
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_detalle.getListar()) {
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
            System.err.println("Error para cargar detalle: " + e.getMessage());
        }

        tb_detalle.setModel(modelo);
        tb_detalle.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_detalle.getColumnModel();
        if (columnas.getColumnCount() >= 5) {
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(1).setCellRenderer(centro);
            columnas.getColumn(3).setCellRenderer(centro);
            columnas.getColumn(4).setCellRenderer(centro);
        }
    }

    
    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_detalle.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR, RETIRAR Y BORRAR; SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true, true);

            String idMat = tb_detalle.getValueAt(fila, 0).toString();
            String idCur = tb_detalle.getValueAt(fila, 1).toString();
            try {
                ResultSet rs = crud_detalle.getBuscar(idMat + "|" + idCur);
                rs.next();

                for (int i = 0; i < arr_matricula.size(); i++) {
                    if (arr_matricula.get(i)[0].equals(idMat)) {
                        cbo_matricula.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < arr_curso.size(); i++) {
                    if (arr_curso.get(i)[0].equals(idCur)) {
                        cbo_curso.setSelectedIndex(i);
                        break;
                    }
                }

                Object nota = rs.getObject("nota");
                txt_nota.setText(nota == null ? "" : nota.toString());
                txt_estado.setText(String.valueOf(rs.getInt("estado")));

                cbo_matricula.setEnabled(false);
                cbo_curso.setEnabled(false);
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar detalle: " + ex.getMessage());
            }
        }
    }
    
    private boolean ValidarFormulario() {

        if (cbo_matricula.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una matrícula");
            cbo_matricula.requestFocus();
            return false;
        }

        if (cbo_curso.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso");
            cbo_curso.requestFocus();
            return false;
        }

        String textoNota = txt_nota.getText().trim();

        // La nota es opcional
        if (!validacion.estaVacio(textoNota)) {

            try {
                double nota = Double.parseDouble(textoNota);

                if (nota < 0 || nota > 20) {
                    JOptionPane.showMessageDialog(this,
                            "La nota debe estar entre 0 y 20");
                    txt_nota.requestFocus();
                    return false;
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese una nota numérica válida");
                txt_nota.requestFocus();
                return false;
            }
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

        // RETIRAR CURSO
        if (e.getSource() == btn_retirar) {

            if (!btn_retirar.isEnabled()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un detalle de matrícula");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de retirar el curso?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {

                String idMat = arr_matricula.get(
                        cbo_matricula.getSelectedIndex()
                )[0];

                String idCur = arr_curso.get(
                        cbo_curso.getSelectedIndex()
                )[0];

                crud_detalle.Retirar(
                        Integer.parseInt(idMat),
                        Integer.parseInt(idCur)
                );

                JOptionPane.showMessageDialog(this, "Curso retirado");

                MostrarDatos();
                LimpiarDatos();
            }

            return;
        }

        // BORRAR DETALLE
        if (e.getSource() == btn_borrar) {

            if (!btn_borrar.isEnabled()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un detalle de matrícula");
                return;
            }

            String idMat = arr_matricula.get(
                    cbo_matricula.getSelectedIndex()
            )[0];

            String idCur = arr_curso.get(
                    cbo_curso.getSelectedIndex()
            )[0];

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de borrar el registro?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {

                crud_detalle.Borrar(idMat + "|" + idCur);

                JOptionPane.showMessageDialog(this, "Detalle eliminado");

                MostrarDatos();
                LimpiarDatos();
            }

            return;
        }

        // SOLO AGREGAR O EDITAR
        if (e.getSource() != btn_agregar &&
                e.getSource() != btn_editar) {
            return;
        }

        if (e.getSource() == btn_editar && !btn_editar.isEnabled()) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un detalle de matrícula");
            return;
        }

        if (!ValidarFormulario()) {
            return;
        }

        String idMat = arr_matricula.get(
                cbo_matricula.getSelectedIndex()
        )[0];

        String idCur = arr_curso.get(
                cbo_curso.getSelectedIndex()
        )[0];

        Double nota = null;

        if (!validacion.estaVacio(txt_nota.getText())) {
            nota = Double.parseDouble(txt_nota.getText().trim());
        }

        DetalleMatricula detalle = new DetalleMatricula();

        detalle.setIdMatricula(Integer.parseInt(idMat));
        detalle.setIdCurso(Integer.parseInt(idCur));
        detalle.setNota(nota);
        detalle.setEstado(Integer.parseInt(txt_estado.getText()));

        if (e.getSource() == btn_agregar) {

            crud_detalle.Registrar(detalle);

            JOptionPane.showMessageDialog(this,
                    "Curso agregado a la matrícula");

        } else if (e.getSource() == btn_editar) {

            crud_detalle.EditarPorId(detalle);

            JOptionPane.showMessageDialog(this,
                    "Detalle actualizado");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        cbo_matricula.setEnabled(true);
        cbo_curso.setEnabled(true);
        tb_detalle.clearSelection();
        cbo_matricula.setSelectedIndex(0);
        cbo_curso.setSelectedIndex(0);
        txt_nota.setText("");
        txt_estado.setText("1");
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false, false);
    }

    public static AppDetalleMatricula getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppDetalleMatricula();
        }
        return instancia;
    }
}