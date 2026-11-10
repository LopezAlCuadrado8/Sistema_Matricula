package view;

import com.mysql.jdbc.ResultSetMetaData;
import model.Curso;

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

import controller.CRUDCurso;

public class AppCurso extends JInternalFrame implements ActionListener {
    private static AppCurso instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4;
    private JTextField txt_id, txt_nombre, txt_creditos, txt_horas;
    private JComboBox cbo_carrera;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_curso;
    private JScrollPane scr_curso;

    private final CRUDCurso crud_curso = new CRUDCurso();
    private ArrayList<String[]> arr_carrera;

    public AppCurso() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - CURSO");
        this.setSize(460, 540);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("CURSOS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Nombre");
        lbl_1.setBounds(20, 60, 100, 25);
        txt_nombre = new JTextField();
        txt_nombre.setBounds(130, 60, 300, 25);

        lbl_2 = new JLabel("Créditos");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_creditos = new JTextField();
        txt_creditos.setHorizontalAlignment(JTextField.CENTER);
        txt_creditos.setBounds(130, 100, 80, 25);

        lbl_3 = new JLabel("Horas");
        lbl_3.setBounds(230, 100, 60, 25);
        txt_horas = new JTextField();
        txt_horas.setHorizontalAlignment(JTextField.CENTER);
        txt_horas.setBounds(290, 100, 80, 25);

        lbl_4 = new JLabel("Carrera");
        lbl_4.setBounds(20, 140, 100, 25);
        cbo_carrera = new JComboBox();
        cbo_carrera.setBounds(130, 140, 300, 25);

        arr_carrera = this.ObtenerCarreras();
        for (String[] c : arr_carrera)
            cbo_carrera.addItem(c[1]);

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
        btn_cerrar.setBounds(130, 450, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_curso = new JTable();
        tb_curso.setRowHeight(20);
        tb_curso.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_curso = new JScrollPane(tb_curso);
        scr_curso.setBounds(10, 220, 430, 220);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(txt_nombre);
        this.add(lbl_2);
        this.add(txt_creditos);
        this.add(lbl_3);
        this.add(txt_horas);
        this.add(lbl_4);
        this.add(cbo_carrera);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_curso);

        ControladorTxt ctxt = new ControladorTxt();
        txt_nombre.addKeyListener(ctxt);
        txt_creditos.addKeyListener(ctxt);
        txt_horas.addKeyListener(ctxt);

        ControladorClick click = new ControladorClick();
        tb_curso.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerCarreras() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione carrera"});
        try (ResultSet rs = crud_curso.getListarCarreras()) {
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

        try (ResultSet rs = crud_curso.getListar()) {
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
            System.err.println("Error para cargar cursos: " + e.getMessage());
        }

        tb_curso.setModel(modelo);
        tb_curso.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_curso.getColumnModel();
        if (columnas.getColumnCount() >= 5) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(2).setCellRenderer(centro);
            columnas.getColumn(3).setCellRenderer(centro);
        }
    }

    private class ControladorTxt implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txt_nombre && txt_nombre.getText().length() == 60) {
                e.consume();
            } else if (e.getSource() == txt_creditos) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txt_creditos.getText().length() >= 2) e.consume();
            } else if (e.getSource() == txt_horas) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || txt_horas.getText().length() >= 3) e.consume();
            }
        }
        @Override public void keyPressed(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_curso.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_curso.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_curso.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_curso")));
                txt_nombre.setText(rs.getString("nombre"));
                txt_creditos.setText(String.valueOf(rs.getInt("creditos")));
                txt_horas.setText(String.valueOf(rs.getInt("horas")));

                int idCar = rs.getInt("id_carrera");
                for (int i = 0; i < arr_carrera.size(); i++) {
                    if (arr_carrera.get(i)[0].equals(String.valueOf(idCar))) {
                        cbo_carrera.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar curso: " + ex.getMessage());
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
                JOptionPane.showMessageDialog(null, "Ingrese nombre del curso");
                txt_nombre.requestFocus();
                return;
            }
            if (txt_creditos.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese créditos");
                txt_creditos.requestFocus();
                return;
            }
            int cred;
            try {
                cred = Integer.parseInt(txt_creditos.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Créditos debe ser numérico");
                txt_creditos.requestFocus();
                return;
            }
            if (cred < 1 || cred > 10) {
                JOptionPane.showMessageDialog(null, "Créditos debe estar entre 1 y 10");
                txt_creditos.requestFocus();
                return;
            }
            if (txt_horas.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese horas");
                txt_horas.requestFocus();
                return;
            }
            int hor;
            try {
                hor = Integer.parseInt(txt_horas.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Horas debe ser numérico");
                txt_horas.requestFocus();
                return;
            }
            if (hor < 1 || hor > 200) {
                JOptionPane.showMessageDialog(null, "Horas debe estar entre 1 y 200");
                txt_horas.requestFocus();
                return;
            }
            if (cbo_carrera.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione carrera");
                cbo_carrera.requestFocus();
                return;
            }

            Curso curso = new Curso();
            curso.setNombre(txt_nombre.getText().trim());
            curso.setCreditos(cred);
            curso.setHoras(hor);
            String nomCar = cbo_carrera.getSelectedItem().toString();
            for (String[] c : arr_carrera) {
                if (c[1].equals(nomCar)) {
                    curso.setIdCarrera(Integer.parseInt(c[0]));
                    break;
                }
            }

            if (e.getSource() == btn_agregar) {
                crud_curso.Registrar(curso);
                JOptionPane.showMessageDialog(null, "Curso Registrado");
            } else if (e.getSource() == btn_editar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un curso de la tabla");
                    return;
                }
                curso.setIdCurso(Integer.parseInt(txt_id.getText()));
                crud_curso.EditarPorId(curso);
                JOptionPane.showMessageDialog(null, "Curso Actualizado");
            } else if (e.getSource() == btn_borrar) {
                if (txt_id.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Seleccione un curso de la tabla");
                    return;
                }
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro de borrar el registro?", "SENATI", JOptionPane.YES_NO_OPTION);
                if (opc == JOptionPane.YES_OPTION) {
                    crud_curso.BorrarPorId(Integer.parseInt(txt_id.getText()));
                    JOptionPane.showMessageDialog(null, "Curso Eliminado");
                }
            }

            MostrarDatos();
            LimpiarDatos();
        }
    }

    private void LimpiarDatos() {
        tb_curso.clearSelection();
        txt_id.setText("");
        txt_nombre.setText("");
        txt_creditos.setText("");
        txt_horas.setText("");
        cbo_carrera.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_nombre.requestFocus();
    }

    public static AppCurso getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppCurso();
        }
        return instancia;
    }
}