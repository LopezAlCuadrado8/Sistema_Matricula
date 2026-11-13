package view;
import model.UsuarioLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class AppMenu extends JFrame implements ActionListener {
    // Controles
    private JDesktopPane dsk_escritorio;

    private JMenuBar mb_principal;

    private JMenu mn_mantenimiento;
    private JMenu mn_academico;
    private JMenu mn_matriculas;

    // Mantenimiento
    private JMenuItem mi_usuario;
    private JMenuItem mi_aula;
    private JMenuItem mi_bloque;

    // Académico
    private JMenuItem mi_carrera;
    private JMenuItem mi_curso;
    private JMenuItem mi_estudiante;
    private JMenuItem mi_docente;
    private JMenuItem mi_horario;

    // Matrículas
    private JMenuItem mi_matricula;
    private JMenuItem mi_detalle_matricula;
    private JMenuItem mi_pago;

    private JMenuItem mi_salir;
    private JSeparator msp_1, msp_2, msp_3;
    
    private final UsuarioLogin usuario;

    public AppMenu(UsuarioLogin usuario) {
        super();

        this.usuario = usuario;

        ConfigurarFormulario();
        ConfigurarControles();

        this.setTitle(
            "Instituto San José - SISTEMA DE MATRÍCULAS | Usuario: "
            + usuario.getNombres()
        );
    }

    private void ConfigurarFormulario() {
        this.setTitle("SENATI - SISTEMA DE MATRÍCULAS");
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        dsk_escritorio = new JDesktopPane();

        mb_principal = new JMenuBar();

        // ===== MENÚ MANTENIMIENTO =====
        mn_mantenimiento = new JMenu("Mantenimiento");

        mi_usuario = new JMenuItem("Usuarios");
        mi_usuario.addActionListener(this);

        mi_aula = new JMenuItem("Aulas");
        mi_aula.addActionListener(this);

        mi_bloque = new JMenuItem("Bloques");
        mi_bloque.addActionListener(this);

        msp_1 = new JSeparator();

        mi_salir = new JMenuItem("Salir");
        mi_salir.addActionListener(this);

        mn_mantenimiento.add(mi_usuario);
        mn_mantenimiento.add(mi_aula);
        mn_mantenimiento.add(mi_bloque);
        mn_mantenimiento.add(msp_1);
        mn_mantenimiento.add(mi_salir);

        // ===== MENÚ ACADÉMICO =====
        mn_academico = new JMenu("Académico");

        mi_carrera = new JMenuItem("Carreras");
        mi_carrera.addActionListener(this);

        mi_curso = new JMenuItem("Cursos");
        mi_curso.addActionListener(this);

        mi_estudiante = new JMenuItem("Estudiantes");
        mi_estudiante.addActionListener(this);

        mi_docente = new JMenuItem("Docentes");
        mi_docente.addActionListener(this);

        mi_horario = new JMenuItem("Horarios");
        mi_horario.addActionListener(this);

        mn_academico.add(mi_carrera);
        mn_academico.add(mi_curso);
        mn_academico.add(mi_estudiante);
        mn_academico.add(mi_docente);
        mn_academico.add(mi_horario);

        // ===== MENÚ MATRÍCULAS =====
        mn_matriculas = new JMenu("Matrículas");

        mi_matricula = new JMenuItem("Registrar Matrícula");
        mi_matricula.addActionListener(this);

        mi_detalle_matricula = new JMenuItem("Detalle de Matrícula");
        mi_detalle_matricula.addActionListener(this);

        mi_pago = new JMenuItem("Pagos");
        mi_pago.addActionListener(this);

        mn_matriculas.add(mi_matricula);
        mn_matriculas.add(mi_detalle_matricula);
        mn_matriculas.add(mi_pago);

        mb_principal.add(mn_mantenimiento);
        mb_principal.add(mn_academico);
        mb_principal.add(mn_matriculas);

        setJMenuBar(mb_principal);

        this.add(dsk_escritorio);
    }

    public void AbrirFrm(JInternalFrame frm) {
        if (!frm.isVisible()) {
            dsk_escritorio.add(frm);
            frm.setLocation((dsk_escritorio.getWidth() - frm.getWidth()) / 2, (dsk_escritorio.getHeight() - frm.getHeight()) / 2);
            frm.setVisible(true);
        } else {
            frm.toFront();
            frm.requestFocus();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ===== MANTENIMIENTO =====
        if (e.getSource() == mi_usuario) {
            AbrirFrm(AppUsuario.getInstancia());
        } else if (e.getSource() == mi_aula) {
            AbrirFrm(AppAula.getInstancia());
        } else if (e.getSource() == mi_bloque) {
            AbrirFrm(AppBloque.getInstancia());

        // ===== ACADÉMICO =====
        } else if (e.getSource() == mi_carrera) {
            AbrirFrm(AppCarrera.getInstancia());
        } else if (e.getSource() == mi_curso) {
            AbrirFrm(AppCurso.getInstancia());
        } else if (e.getSource() == mi_estudiante) {
            AbrirFrm(AppEstudiante.getInstancia());
        } else if (e.getSource() == mi_docente) {
            AbrirFrm(AppDocente.getInstancia());
        } else if (e.getSource() == mi_horario) {
            AbrirFrm(AppHorario.getInstancia());

        // ===== MATRÍCULAS =====
        } else if (e.getSource() == mi_matricula) {
            AbrirFrm(AppMatricula.getInstancia());
        } else if (e.getSource() == mi_detalle_matricula) {
            AbrirFrm(AppDetalleMatricula.getInstancia());
        } else if (e.getSource() == mi_pago) {
            AbrirFrm(AppPago.getInstancia());

        // ===== SALIR =====
        } else if (e.getSource() == mi_salir) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView login = new LoginView();
            login.setVisible(true);
        });

    }
}