package view;

import java.sql.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;
import model.Bloque;

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
import java.util.Date;
import javax.swing.JButton;
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

import controller.CRUDBloque;

public class AppBloque extends JInternalFrame implements ActionListener {
    private static AppBloque instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3;
    private JTextField txt_id, txt_nombre;
    private JDateChooser dc_inicio, dc_fin;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_bloque;
    private JScrollPane scr_bloque;

    private final CRUDBloque crud_bloque = new CRUDBloque();
    private final ValidacionController validacion =
            new ValidacionController();

    public AppBloque() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("Instituto San José - BLOQUE");
        this.setSize(460, 500);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("BLOQUES");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Nombre");
        lbl_1.setBounds(20, 60, 100, 25);
        txt_nombre = new JTextField();
        txt_nombre.setBounds(130, 60, 300, 25);

        lbl_2 = new JLabel("Fecha Inicio");
        lbl_2.setBounds(20, 100, 100, 25);
        dc_inicio = new JDateChooser();
        dc_inicio.setDateFormatString("dd/MM/yyyy");
        dc_inicio.setBounds(130, 100, 140, 25);

        lbl_3 = new JLabel("Fecha Fin");
        lbl_3.setBounds(20, 140, 100, 25);
        dc_fin = new JDateChooser();
        dc_fin.setDateFormatString("dd/MM/yyyy");
        dc_fin.setBounds(130, 140, 140, 25);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 180, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(120, 180, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(230, 180, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(340, 180, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(130, 410, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_bloque = new JTable();
        tb_bloque.setRowHeight(20);
        tb_bloque.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_bloque = new JScrollPane(tb_bloque);
        scr_bloque.setBounds(10, 220, 430, 180);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(txt_nombre);
        this.add(lbl_2);
        this.add(dc_inicio);
        this.add(lbl_3);
        this.add(dc_fin);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_bloque);

        ((AbstractDocument) txt_nombre.getDocument()).setDocumentFilter(
        	    new ValidationDocumentFilter("ALFANUMERICO", 30)
        	);

        ControladorClick click = new ControladorClick();
        tb_bloque.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_borrar.setEnabled(borrar);
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_bloque.getListar()) {
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
            System.err.println("Error para cargar bloques: " + e.getMessage());
        }

        tb_bloque.setModel(modelo);
        tb_bloque.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_bloque.getColumnModel();
        if (columnas.getColumnCount() >= 4) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(2).setCellRenderer(centro);
            columnas.getColumn(3).setCellRenderer(centro);
        }
    }

  

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_bloque.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_bloque.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_bloque.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_bloque")));
                txt_nombre.setText(rs.getString("nombre"));
                dc_inicio.setDate(rs.getDate("fecha_inicio"));
                dc_fin.setDate(rs.getDate("fecha_fin"));
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar bloque: " + ex.getMessage());
            }
        }
    }
    
    private boolean ValidarFormulario() {

        String nombre = txt_nombre.getText().trim();

        if (validacion.estaVacio(nombre)) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese el nombre del bloque.");
            txt_nombre.requestFocus();
            return false;
        }

        if (!validacion.validarLongitud(nombre, 1, 30)) {
            JOptionPane.showMessageDialog(this,
                    "El nombre debe tener máximo 30 caracteres.");
            txt_nombre.requestFocus();
            return false;
        }

        if (dc_inicio.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione la fecha de inicio.");
            return false;
        }

        if (dc_fin.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione la fecha de fin.");
            return false;
        }

        if (!dc_inicio.getDate().before(dc_fin.getDate())) {
            JOptionPane.showMessageDialog(this,
                    "La fecha de inicio debe ser anterior a la fecha de fin.");
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

        // ELIMINAR
        if (e.getSource() == btn_borrar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un bloque de la tabla.");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de borrar el registro?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {

                crud_bloque.BorrarPorId(
                        Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(this, "Bloque eliminado.");

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

        if (!ValidarFormulario()) {
            return;
        }

        Bloque bloque = new Bloque();

        bloque.setNombre(txt_nombre.getText().trim());

        Date fi = dc_inicio.getDate();
        Date ff = dc_fin.getDate();

        bloque.setFechaInicio(new java.sql.Date(fi.getTime()));
        bloque.setFechaFin(new java.sql.Date(ff.getTime()));

        if (e.getSource() == btn_agregar) {

            crud_bloque.Registrar(bloque);

            JOptionPane.showMessageDialog(this, "Bloque registrado.");

        } else if (e.getSource() == btn_editar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un bloque de la tabla.");
                return;
            }

            bloque.setIdBloque(Integer.parseInt(txt_id.getText()));

            crud_bloque.EditarPorId(bloque);

            JOptionPane.showMessageDialog(this, "Bloque actualizado.");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        tb_bloque.clearSelection();
        txt_id.setText("");
        txt_nombre.setText("");
        dc_inicio.setDate(null);
        dc_fin.setDate(null);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_nombre.requestFocus();
    }

    public static AppBloque getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppBloque();
        }
        return instancia;
    }
}