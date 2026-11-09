package view;

import com.mysql.jdbc.ResultSetMetaData;
import com.toedter.calendar.JDateChooser;
import model.Personal;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import controller.CRUDCargo;
import controller.CRUDPersonal;

public class AppPersonal extends JInternalFrame implements ActionListener {
    private static AppPersonal instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4, lbl_5, lbl_6, lbl_7, lbl_8;
    private JTextField txt_dni, txt_nombre, txt_app, txt_apm, txt_sueldo;

    private JDateChooser dc_fn;

    private JRadioButton rbt_genero_f, rbt_genero_m;
    private ButtonGroup bg_genero;

    private JComboBox cbo_cargo;

    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;

    private JTable tb_personal;
    private JScrollPane scr_personal;
    
    private final CRUDPersonal crud_personal = new CRUDPersonal();
    private final CRUDCargo crud_cargo = new CRUDCargo();
    
    private ArrayList<String[]> arr_cargo;

    public AppPersonal() {
        super();

        ConfigurarFormulario();
        ConfigurarControles();
        
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - ETI");
        this.setSize(430, 540);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel();
        lbl_titulo.setText("PERSONAL");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel();
        lbl_1.setText("Número DNI");
        lbl_1.setBounds(20, 50, 120, 25);

        txt_dni = new JTextField();
        txt_dni.setHorizontalAlignment(JTextField.CENTER);
        txt_dni.setBounds(150, 50, 80, 25);

        lbl_2 = new JLabel();
        lbl_2.setText("Apellido Paterno");
        lbl_2.setBounds(20, 80, 110, 25);

        txt_app = new JTextField();
        txt_app.setBounds(20, 110, 110, 25);

        lbl_3 = new JLabel();
        lbl_3.setText("Apellido Materno");
        lbl_3.setBounds(150, 80, 110, 25);

        txt_apm = new JTextField();
        txt_apm.setBounds(150, 110, 110, 25);

        lbl_4 = new JLabel();
        lbl_4.setText("Nombre");
        lbl_4.setBounds(280, 80, 110, 25);

        txt_nombre = new JTextField();
        txt_nombre.setBounds(280, 110, 110, 25);

        lbl_5 = new JLabel();
        lbl_5.setText("Género");
        lbl_5.setBounds(20, 140, 110, 25);

        bg_genero = new ButtonGroup();

        rbt_genero_f = new JRadioButton("Femenino");
        rbt_genero_f.setBounds(150, 140, 110, 25);
        bg_genero.add(rbt_genero_f);

        rbt_genero_m = new JRadioButton("Masculino");
        rbt_genero_m.setBounds(280, 140, 110, 25);
        bg_genero.add(rbt_genero_m);

        lbl_6 = new JLabel();
        lbl_6.setText("Fecha Nacimiento");
        lbl_6.setBounds(20, 170, 110, 25);

        dc_fn = new JDateChooser();
        dc_fn.setDateFormatString("dd/MM/yyyy");
        dc_fn.setBounds(20, 200, 110, 25);

        lbl_7 = new JLabel();
        lbl_7.setText("Sueldo");
        lbl_7.setBounds(150, 170, 110, 25);

        txt_sueldo = new JTextField();
        txt_sueldo.setHorizontalAlignment(JTextField.RIGHT);
        txt_sueldo.setBounds(150, 200, 110, 25);

        lbl_8 = new JLabel();
        lbl_8.setText("Cargo");
        lbl_8.setBounds(280, 170, 110, 25);

        cbo_cargo = new JComboBox();
        cbo_cargo.setBounds(280, 200, 110, 25);

        arr_cargo = this.ObtenerCargo();
        
        arr_cargo.forEach(c -> cbo_cargo.addItem(c[1]));
        
        /*
        for (String c[] : arr_cargo)
            cbo_cargo.addItem(c[1]);*/
        
        
        btn_nuevo = new JButton();
        btn_nuevo.setText("NUEVO");
        btn_nuevo.setBounds(10, 240, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton();
        btn_agregar.setText("AGREGAR");
        btn_agregar.setBounds(110, 240, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton();
        btn_editar.setText("EDITAR");
        btn_editar.setBounds(210, 240, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton();
        btn_borrar.setText("BORRAR");
        btn_borrar.setBounds(310, 240, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton();
        btn_cerrar.setText("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(110, 450, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_personal = new JTable();
        
        tb_personal.setRowHeight(20);
        tb_personal.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_personal = new JScrollPane(tb_personal);
        scr_personal.setBounds(10, 280, 390, 150);

        // Agregar controles
        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(txt_dni);
        this.add(lbl_2);
        this.add(txt_app);
        this.add(lbl_3);
        this.add(txt_apm);
        this.add(lbl_4);
        this.add(txt_nombre);
        this.add(lbl_5);
        this.add(rbt_genero_f);
        this.add(rbt_genero_m);
        this.add(lbl_6);
        this.add(dc_fn);
        this.add(lbl_7);
        this.add(txt_sueldo);
        this.add(lbl_8);
        this.add(cbo_cargo);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_personal);

        ControladorTxt ctxt = new ControladorTxt();

        txt_dni.addKeyListener(ctxt);
        txt_app.addKeyListener(ctxt);
        txt_apm.addKeyListener(ctxt);
        txt_nombre.addKeyListener(ctxt);

        ControladorClick click = new ControladorClick();

        tb_personal.addMouseListener(click);
    }
    
    private ArrayList<String[]> ObtenerCargo() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        
        arr_lista.add(new String[]{"", "Seleccione cargo"});
        
        try (ResultSet rs = crud_cargo.getListar()) {
            while (rs.next()) {
                arr_lista.add(new String[]{
                    rs.getString("codigo_cargo"), rs.getString("cargo")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cargos: " + e.getMessage());
        }
        
        return arr_lista;
    }
    
    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try (ResultSet rs = crud_personal.getListar()) {
            ResultSetMetaData meta = (ResultSetMetaData) rs.getMetaData();
            
            int nc = meta.getColumnCount();
            
            // Columnas
            for (int i = 1; i <= nc; i++)
                modelo.addColumn(meta.getColumnLabel(i));
            
            // Registros
            while (rs.next()) {
                Object[] arr_fila = new Object[nc];
                
                for (int i = 0; i < nc; i++)
                    arr_fila[i] = rs.getObject(i + 1);
                
                modelo.addRow(arr_fila);
            }
        } catch (SQLException e) {
            System.err.println("Error para cargar el personal: " + e.getMessage());
        }
        
        tb_personal.setModel(modelo);
        tb_personal.setRowHeight(22);

        // Configurar alineación
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumnModel columnas = tb_personal.getColumnModel();

        columnas.getColumn(0).setPreferredWidth(50);
        columnas.getColumn(0).setCellRenderer(centro);

        columnas.getColumn(1).setPreferredWidth(120);
    }

    private class ControladorTxt implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getSource() == txt_dni && txt_dni.getText().length() == 8) {
                e.consume();
            } else if (e.getSource() == txt_app && txt_app.getText().length() == 20) {
                e.consume();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class ControladorClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int registro = tb_personal.getSelectedRow();

            txt_dni.setEditable(false);

            String dni = (String) tb_personal.getValueAt(registro, 0);

            try {
                ResultSet rs = crud_personal.getBuscar(dni);
                
                rs.next();
                
                String app = rs.getString("ap_paterno");
                String apm = rs.getString("ap_materno");
                String nom = rs.getString("nombre");
                String gen = rs.getString("genero");
                Date fn = rs.getDate("fecha_nacimiento");
                float sue = rs.getFloat("sueldo");
                String cc = rs.getString("codigo_cargo");

                txt_dni.setText(dni);
                txt_app.setText(app);
                txt_apm.setText(apm);
                txt_nombre.setText(nom);

                if (gen.equals("M"))
                    rbt_genero_m.setSelected(true);
                else if (gen.equals("F"))
                    rbt_genero_f.setSelected(true);
                
                dc_fn.setDate(fn);

                txt_sueldo.setText(String.valueOf(sue));
                
                String nc = arr_cargo.stream()
                        .filter(c -> cc.equals(c[0]))
                        .map(c -> c[1])
                        .findFirst().orElse("");
                
                /*
                String nc = "";
                
                for (String[] c : arr_cargo) {
                    if (cc.equals(c[0])) {
                        nc = c[1];
                        break;
                    }
                }*/
                
                cbo_cargo.setSelectedItem(nc);
            } catch (SQLException ex) {
                
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
            // Validar Núm. DNI
            if (txt_dni.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Ingrese número de DNI");
                txt_dni.requestFocus();
                return;
            }

            // Validar Ap. Paterno
            
            // Validar Ap. Materno
            
            // Validar Nombre
            
            // Validar Género
            if (!rbt_genero_f.isSelected() && !rbt_genero_m.isSelected()) {
                JOptionPane.showMessageDialog(null, "Elija un género");
                return;
            }
            
            // Validar Sueldo
            
            // Validar Cargo
            if (cbo_cargo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Seleccione cargo");
                cbo_cargo.requestFocus();
                return;
            }

            Personal personal = new Personal();

            personal.setDNI(txt_dni.getText());
            personal.setApPaterno(txt_app.getText());
            personal.setApMaterno(txt_apm.getText());
            personal.setNombre(txt_nombre.getText());

            if (rbt_genero_f.isSelected()) {
                personal.setGenero('F');
            } else if (rbt_genero_m.isSelected()) {
                personal.setGenero('M');
            }

            Date fn_1 = dc_fn.getDate();
            long fn_2 = fn_1.getTime();
            java.sql.Date fn_3 = new java.sql.Date(fn_2);

            personal.setFechaNacimiento(fn_3);

            personal.setSueldo(Float.parseFloat(txt_sueldo.getText()));

            String nc = cbo_cargo.getSelectedItem().toString();
            
            String cc = arr_cargo.stream()
                        .filter(c -> nc.equals(c[1]))
                        .map(c -> c[0])
                        .findFirst().orElse("");
            
            /*
            String cc = "";
            
            for (String[] c : arr_cargo) {
                if (nc.equals(c[1])) {
                    cc = c[0];
                    break;
                }
            }*/
            

            personal.setCodigoCargo(cc);

            if (e.getSource() == btn_agregar) {
                crud_personal.Registrar(personal);
                JOptionPane.showMessageDialog(null, "Personal Registrado");
            } else if (e.getSource() == btn_editar) {
                crud_personal.Editar(personal);
                JOptionPane.showMessageDialog(null, "Personal Actualizado");
            } else if (e.getSource() == btn_borrar) {
                int opc = JOptionPane.showConfirmDialog(null, "¿Seguro de borrar el registro?", "SENATI", JOptionPane.YES_NO_OPTION);

                if (opc == JOptionPane.YES_OPTION) {
                    crud_personal.Borrar(personal.getDNI());
                    JOptionPane.showMessageDialog(null, "Personal Eliminado");
                }
            }

            MostrarDatos();
            
            LimpiarDatos();
        }
    }

    private void LimpiarDatos() {
        txt_dni.setEditable(true);

        tb_personal.clearSelection();
        
        JTextField txt;

        for (int i = 0; i < this.getContentPane().getComponents().length; i++) {
            if (this.getContentPane().getComponent(i) instanceof JTextField jTextField) {
                txt = jTextField;
                txt.setText("");
            }
        }
        
        bg_genero.clearSelection();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date fecha;
        try {
            fecha = sdf.parse("2000-01-01");
            dc_fn.setDate(fecha);
        } catch (ParseException ex) {
            Logger.getLogger(AppPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }

        cbo_cargo.setSelectedIndex(0);

        txt_dni.requestFocus();
    }
    
    public static AppPersonal getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppPersonal();
        }
        
        return instancia;
    }
}
