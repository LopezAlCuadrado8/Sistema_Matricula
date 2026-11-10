package view;

import com.mysql.jdbc.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;
import model.Pago;

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

import controller.CRUDPago;

public class AppPago extends JInternalFrame implements ActionListener {
    private static AppPago instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4;
    private JTextField txt_id, txt_monto, txt_metodo;
    private JDateChooser dc_fecha;
    private JComboBox cbo_matricula;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_pago;
    private JScrollPane scr_pago;

    private final CRUDPago crud_pago = new CRUDPago();
    private ArrayList<String[]> arr_matricula;

    public AppPago() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - PAGO");
        this.setSize(540, 520);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("PAGOS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Matrícula");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_matricula = new JComboBox();
        cbo_matricula.setBounds(130, 60, 380, 25);

        arr_matricula = this.ObtenerMatriculas();
        for (String[] m : arr_matricula)
            cbo_matricula.addItem(m[1]);

        lbl_2 = new JLabel("Monto");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_monto = new JTextField();
        txt_monto.setHorizontalAlignment(JTextField.RIGHT);
        txt_monto.setBounds(130, 100, 100, 25);

        lbl_3 = new JLabel("Fecha");
        lbl_3.setBounds(20, 140, 100, 25);
        dc_fecha = new JDateChooser();
        dc_fecha.setDateFormatString("dd/MM/yyyy");
        dc_fecha.setBounds(130, 140, 140, 25);

        lbl_4 = new JLabel("Método");
        lbl_4.setBounds(290, 140, 60, 25);
        txt_metodo = new JTextField();
        txt_metodo.setBounds(360, 140, 150, 25);

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
        btn_cerrar.setBounds(170, 430, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_pago = new JTable();
        tb_pago.setRowHeight(20);
        tb_pago.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_pago = new JScrollPane(tb_pago);
        scr_pago.setBounds(10, 220, 510, 180);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_matricula);
        this.add(lbl_2);
        this.add(txt_monto);
        this.add(lbl_3);
        this.add(dc_fecha);
        this.add(lbl_4);
        this.add(txt_metodo);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_pago);

        ControladorTxt ctxt = new ControladorTxt();
        txt_monto.addKeyListener(ctxt);
        txt_metodo.addKeyListener(ctxt);

        ControladorClick click = new ControladorClick();
        tb_pago.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerMatriculas() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione matrícula"});
        try (ResultSet rs = crud_pago.getListarMatriculas()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_matricula"), rs.getString("descripcion") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener matrículas: " + e.getMessage());
        }
        return arr_lista;
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_pago.getListar()) {
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
            System.err.println("Error para cargar pagos: " + e.getMessage());
        }

        tb_pago.setModel(modelo);
        tb_pago.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_pago.getColumnModel();
        if (columnas.getColumnCount() >= 7) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(1).setCellRenderer(centro);
            columnas.getColumn(2).setCellRenderer(centro);
            columnas.getColumn(4).setCellRenderer(centro);
            columnas.getColumn(5).setCellRenderer(centro);
        }
    }

    private class ControladorTxt implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txt_monto) {
                char c = e.getKeyChar();
                if ((!Character.isDigit(c) && c != '.') || txt_monto.getText().length() >= 10) {
                    e.consume();
                }
            } else if (e.getSource() == txt_metodo && txt_metodo.getText().length() >= 30) {
                e.consume();
            }
        }
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_pago.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_pago.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_pago.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_pago")));
                txt_monto.setText(String.valueOf(rs.getDouble("monto")));
                dc_fecha.setDate(rs.getDate("fecha"));
                txt_metodo.setText(rs.getString("metodo"));

                int idMat = rs.getInt("id_matricula");
                for (int i = 0; i < arr_matricula.size(); i++) {
                    if (arr_matricula.get(i)[0].equals(String.valueOf(idMat))) {
                        cbo_matricula.setSelectedIndex(i);
                        break;
                    }
                }
                cbo_matricula.setEnabled(false);
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar pago: " + ex.getMessage());
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
            if (cbo_matricula.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione matrícula");
                cbo_matricula.requestFocus();
                return;
            }
            if (txt_monto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese monto");
                txt_monto.requestFocus();
                return;
            }
            double monto;
            try {
                monto = Double.parseDouble(txt_monto.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Monto debe ser numérico");
                txt_monto.requestFocus();
                return;
            }
            if (monto <= 0) {
                JOptionPane.showMessageDialog(null, "Monto debe ser mayor a 0");
                txt_monto.requestFocus();
                return;
            }
            if (dc_fecha.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione fecha de pago");
                return;
            }
            if (txt_metodo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese método de pago");
                txt_metodo.requestFocus();
                return;
            }

            Pago pago = new Pago();
            pago.setMonto(monto);
            pago.setFecha(new java.sql.Date(dc_fecha.getDate().getTime()));
            pago.setMetodo(txt_metodo.getText().trim());
            pago.setIdMatricula(Integer.parseInt(arr_matricula.get(cbo_matricula.getSelectedIndex())[0]));

            if (e.getSource() == btn_agregar) {
                crud_pago.Registrar(pago);
                JOptionPane.showMessageDialog(null, "Pago Registrado");
            } else if (e.getSource() == btn_editar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un pago de la tabla");
                    return;
                }
                pago.setIdPago(Integer.parseInt(txt_id.getText()));
                crud_pago.EditarPorId(pago);
                JOptionPane.showMessageDialog(null, "Pago Actualizado");
            } else if (e.getSource() == btn_borrar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un pago de la tabla");
                    return;
                }
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro de borrar el registro?", "SENATI", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    crud_pago.BorrarPorId(Integer.parseInt(txt_id.getText()));
                    JOptionPane.showMessageDialog(null, "Pago Eliminado");
                }
            }

            MostrarDatos();
            LimpiarDatos();
        }
    }

    private void LimpiarDatos() {
        cbo_matricula.setEnabled(true);
        tb_pago.clearSelection();
        txt_id.setText("");
        txt_monto.setText("");
        dc_fecha.setDate(null);
        txt_metodo.setText("");
        cbo_matricula.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_monto.requestFocus();
    }

    public static AppPago getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppPago();
        }
        return instancia;
    }
}