
package view;

import java.sql.SQLException;

import controller.LoginController;
import controller.ValidacionController;
import model.UsuarioLogin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;

public class LoginView extends JFrame implements ActionListener {

    private JLabel lbl_titulo, lbl_correo, lbl_password;
    private JTextField txt_correo;
    private JPasswordField txt_password;
    private JButton btn_ingresar;
    private LoginController loginController;
    private ValidacionController validacionController;

    public LoginView() {
        super();
        
        loginController = new LoginController();
        validacionController = new ValidacionController();

        ConfigurarFormulario();
        ConfigurarControles();
    }

    private void ConfigurarFormulario() {

        this.setTitle("Instituto San José - INICIAR SESIÓN");
        this.setSize(400, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void ConfigurarControles() {

        
        lbl_titulo = new JLabel();
        lbl_titulo.setText("SISTEMA DE MATRICULAS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 22));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(65, 25, 280, 30);

        
        lbl_correo = new JLabel();
        lbl_correo.setText("Correo electronico");
        lbl_correo.setBounds(50, 85, 150, 25);

        txt_correo = new JTextField();
        txt_correo.setBounds(50, 110, 290, 30);

        
        lbl_password = new JLabel();
        lbl_password.setText("Contraseña");
        lbl_password.setBounds(50, 150, 150, 25);

        txt_password = new JPasswordField();
        txt_password.setBounds(50, 175, 290, 30);

        
        btn_ingresar = new JButton();
        btn_ingresar.setText("INICIAR SESION");
        btn_ingresar.setBounds(100, 220, 190, 30);
        btn_ingresar.setBackground(Color.BLUE);
        btn_ingresar.setForeground(Color.WHITE);
        btn_ingresar.addActionListener(this);

        
        this.add(lbl_titulo);
        this.add(lbl_correo);
        this.add(txt_correo);
        this.add(lbl_password);
        this.add(txt_password);
        this.add(btn_ingresar);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btn_ingresar) {

            String correo = txt_correo.getText().trim();
            String password = new String(txt_password.getPassword());

            // Validar campos vacíos
            if (validacionController.estaVacio(correo)
                    || validacionController.estaVacio(password)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ingrese su correo y contraseña."
                );

                return;
            }

            // Validar formato del correo
            if (!validacionController.validarCorreo(correo)) {

                JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un correo electrónico válido."
                );

                txt_correo.requestFocus();
                return;
            }

            try {

                UsuarioLogin usuario =
                    loginController.iniciarSesion(correo, password);

                if (usuario != null) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Bienvenido " + usuario.getNombres()
                    );

                    AppMenu menu = new AppMenu(usuario);
                    menu.setVisible(true);

                    this.dispose();

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Correo o contraseña incorrectos."
                    );

                    txt_password.setText("");
                }

            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(
                    this,
                    "Error al conectar con la base de datos:\n"
                    + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
                );

                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        LoginView ventana = new LoginView();
        ventana.setVisible(true);
    }
}
