package view;

import com.mysql.jdbc.ResultSetMetaData;
import model.Aula;

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

import controller.CRUDAula;

public class AppAula extends JInternalFrame implements ActionListener {
    private static AppAula instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2;
    private JTextField txt_id, txt_nombre, txt_capacidad;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_aula;
    private JScrollPane scr_aula;

    private final CRUDAula crud_aula = new CRUDAula();

    public AppAula() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - AULA");
        this.setSize(430, 480);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("AULAS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Nombre");
        lbl_1.setBounds(20, 60, 100, 25);
        txt_nombre = new JTextField();
        txt_nombre.setBounds(130, 60, 260, 25);

        lbl_2 = new JLabel("Capacidad");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_capacidad = new JTextField();
        txt_capacidad.setHorizontalAlignment(JTextField.CENTER);
        txt_capacidad.setBounds(130, 100, 80, 25);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 140, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(110, 140, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(210, 140, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(310, 140, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(110, 390, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_aula = new JTable();
        tb_aula.setRowHeight(20);
        tb_aula.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_aula = new JScrollPane(tb_aula);
        scr_aula.setBounds(10, 180, 390, 190);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(txt_nombre);
        this.add(lbl_2);
        this.add(txt_capacidad);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_aula);

        ControladorTxt ctxt = new ControladorTxt();
        txt_nombre.addKeyListener(ctxt);
        txt_capacidad.addKeyListener(ctxt);

        ControladorClick click = new ControladorClick();
        tb_aula.addMouseListener(click);
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

        try (ResultSet rs = crud_aula.getListar()) {
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
            System.err.println("Error para cargar aulas: " + e.getMessage());
        }

        tb_aula.setModel(modelo);
        tb_aula.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_aula.getColumnModel();
        if (columnas.getColumnCount() >= 3) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(2).setCellRenderer(centro);
        }
    }

    private class ControladorTxt implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txt_nombre && txt_nombre.getText().length() >= 30) {
                e.consume();
            } else if (e.getSource() == txt_capacidad) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txt_capacidad.getText().length() >= 3) e.consume();
            }
        }
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_aula.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_aula.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_aula.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_aula")));
                txt_nombre.setText(rs.getString("nombre"));
                txt_capacidad.setText(String.valueOf(rs.getInt("capacidad")));
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar aula: " + ex.getMessage());
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
            if (txt_nombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese nombre del aula");
                txt_nombre.requestFocus();
                return;
            }
            if (txt_capacidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese capacidad");
                txt_capacidad.requestFocus();
                return;
            }
            int cap;
            try {
                cap = Integer.parseInt(txt_capacidad.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Capacidad debe ser numérica");
                txt_capacidad.requestFocus();
                return;
            }
            if (cap <= 0) {
                JOptionPane.showMessageDialog(null, "Capacidad debe ser mayor a 0");
                txt_capacidad.requestFocus();
                return;
            }

            Aula aula = new Aula();
            aula.setNombre(txt_nombre.getText().trim());
            aula.setCapacidad(cap);

            if (e.getSource() == btn_agregar) {
                crud_aula.Registrar(aula);
                JOptionPane.showMessageDialog(null, "Aula Registrada");
            } else if (e.getSource() == btn_editar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un aula de la tabla");
                    return;
                }
                aula.setIdAula(Integer.parseInt(txt_id.getText()));
                crud_aula.EditarPorId(aula);
                JOptionPane.showMessageDialog(null, "Aula Actualizada");
            } else if (e.getSource() == btn_borrar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un aula de la tabla");
                    return;
                }
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro de borrar el registro?", "SENATI", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    crud_aula.BorrarPorId(Integer.parseInt(txt_id.getText()));
                    JOptionPane.showMessageDialog(null, "Aula Eliminada");
                }
            }

            MostrarDatos();
            LimpiarDatos();
        }
    }

    private void LimpiarDatos() {
        tb_aula.clearSelection();
        txt_id.setText("");
        txt_nombre.setText("");
        txt_capacidad.setText("");
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_nombre.requestFocus();
    }

    public static AppAula getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppAula();
        }
        return instancia;
    }
}