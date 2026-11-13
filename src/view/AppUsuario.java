package view;

import com.mysql.jdbc.ResultSetMetaData;
import model.Usuario;
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

import controller.CRUDUsuario;

public class AppUsuario extends JInternalFrame implements ActionListener {
    private static AppUsuario instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4;
    private JTextField txt_id, txt_dni, txt_nombres, txt_apellidos, txt_correo;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_usuario;
    private JScrollPane scr_usuario;
    private final ValidacionController validacion =
            new ValidacionController();

    private final CRUDUsuario crud_usuario = new CRUDUsuario();

    public AppUsuario() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("Instituto San José - USUARIO");
        this.setSize(480, 520);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("USUARIOS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("DNI");
        lbl_1.setBounds(20, 60, 100, 25);
        txt_dni = new JTextField();
        txt_dni.setHorizontalAlignment(JTextField.CENTER);
        txt_dni.setBounds(130, 60, 100, 25);

        lbl_2 = new JLabel("Nombres");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_nombres = new JTextField();
        txt_nombres.setBounds(130, 100, 320, 25);

        lbl_3 = new JLabel("Apellidos");
        lbl_3.setBounds(20, 140, 100, 25);
        txt_apellidos = new JTextField();
        txt_apellidos.setBounds(130, 140, 320, 25);

        lbl_4 = new JLabel("Correo");
        lbl_4.setBounds(20, 180, 100, 25);
        txt_correo = new JTextField();
        txt_correo.setBounds(130, 180, 320, 25);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 220, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(120, 220, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(230, 220, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(340, 220, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(140, 430, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_usuario = new JTable();
        tb_usuario.setRowHeight(20);
        tb_usuario.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_usuario = new JScrollPane(tb_usuario);
        scr_usuario.setBounds(10, 260, 450, 160);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(txt_dni);
        this.add(lbl_2);
        this.add(txt_nombres);
        this.add(lbl_3);
        this.add(txt_apellidos);
        this.add(lbl_4);
        this.add(txt_correo);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_usuario);

     // DNI: solamente números, máximo 8
        ((AbstractDocument) txt_dni.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("NUMEROS", 8)
        );

        // Nombres: letras y espacios, máximo 50
        ((AbstractDocument) txt_nombres.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("LETRAS", 50)
        );

        // Apellidos: letras y espacios, máximo 50
        ((AbstractDocument) txt_apellidos.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("LETRAS", 50)
        );

        // Correo: máximo 60 caracteres
        ((AbstractDocument) txt_correo.getDocument()).setDocumentFilter(
            new ValidationDocumentFilter("CORREO", 60)
        );

        ControladorClick click = new ControladorClick();
        tb_usuario.addMouseListener(click);
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

        try (ResultSet rs = crud_usuario.getListar()) {
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
            System.err.println("Error para cargar usuarios: " + e.getMessage());
        }

        tb_usuario.setModel(modelo);
        tb_usuario.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_usuario.getColumnModel();
        if (columnas.getColumnCount() >= 5) {
            columnas.getColumn(0).setPreferredWidth(40);
            columnas.getColumn(0).setCellRenderer(centro);
            columnas.getColumn(1).setCellRenderer(centro);
        }
    }


    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_usuario.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_usuario.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_usuario.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_usuario")));
                txt_dni.setText(rs.getString("dni"));
                txt_nombres.setText(rs.getString("nombres"));
                txt_apellidos.setText(rs.getString("apellidos"));
                txt_correo.setText(rs.getString("correo"));
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar usuario: " + ex.getMessage());
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
            return;
        }

        // Solo validar cuando se agrega o edita
        if (e.getSource() != btn_agregar
                && e.getSource() != btn_editar
                && e.getSource() != btn_borrar) {
            return;
        }

        if (e.getSource() == btn_borrar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un usuario de la tabla."
                );
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro de borrar el registro?",
                "SENATI",
                JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {
                crud_usuario.BorrarPorId(
                    Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(this, "Usuario eliminado.");
            }

            MostrarDatos();
            LimpiarDatos();
            return;
        }

        // Validar datos antes de registrar o editar
        if (!ValidarFormulario()) {
            return;
        }

        Usuario usuario = new Usuario();

        usuario.setDNI(txt_dni.getText().trim());
        usuario.setNombres(txt_nombres.getText().trim());
        usuario.setApellidos(txt_apellidos.getText().trim());
        usuario.setCorreo(txt_correo.getText().trim());

        if (e.getSource() == btn_agregar) {

            crud_usuario.Registrar(usuario);
            JOptionPane.showMessageDialog(this, "Usuario registrado.");

        } else if (e.getSource() == btn_editar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un usuario de la tabla."
                );
                return;
            }

            usuario.setIdUsuario(Integer.parseInt(txt_id.getText()));

            crud_usuario.EditarPorId(usuario);
            JOptionPane.showMessageDialog(this, "Usuario actualizado.");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        tb_usuario.clearSelection();
        txt_id.setText("");
        txt_dni.setText("");
        txt_nombres.setText("");
        txt_apellidos.setText("");
        txt_correo.setText("");
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
        txt_dni.requestFocus();
    }
    
    private boolean ValidarFormulario() {

        String dni = txt_dni.getText().trim();
        String nombres = txt_nombres.getText().trim();
        String apellidos = txt_apellidos.getText().trim();
        String correo = txt_correo.getText().trim();

        if (validacion.estaVacio(dni)) {
            JOptionPane.showMessageDialog(this, "Ingrese el DNI.");
            txt_dni.requestFocus();
            return false;
        }

        if (!validacion.validarDNI(dni)) {
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos.");
            txt_dni.requestFocus();
            return false;
        }

        if (validacion.estaVacio(nombres)) {
            JOptionPane.showMessageDialog(this, "Ingrese los nombres.");
            txt_nombres.requestFocus();
            return false;
        }

        if (!validacion.validarNombre(nombres)) {
            JOptionPane.showMessageDialog(this, "Los nombres contienen caracteres inválidos.");
            txt_nombres.requestFocus();
            return false;
        }

        if (validacion.estaVacio(apellidos)) {
            JOptionPane.showMessageDialog(this, "Ingrese los apellidos.");
            txt_apellidos.requestFocus();
            return false;
        }

        if (!validacion.validarNombre(apellidos)) {
            JOptionPane.showMessageDialog(this, "Los apellidos contienen caracteres inválidos.");
            txt_apellidos.requestFocus();
            return false;
        }

        if (validacion.estaVacio(correo)) {
            JOptionPane.showMessageDialog(this, "Ingrese el correo.");
            txt_correo.requestFocus();
            return false;
        }

        if (!validacion.validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this, "Formato de correo inválido.");
            txt_correo.requestFocus();
            return false;
        }

        return true;
    }

    public static AppUsuario getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppUsuario();
        }
        return instancia;
    }
}