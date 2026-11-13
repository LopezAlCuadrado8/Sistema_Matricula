package view;

import java.sql.ResultSetMetaData;
import model.Horario;

import controller.ValidacionController;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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

import controller.CRUDHorario;

public class AppHorario extends JInternalFrame implements ActionListener {
    private static AppHorario instancia = null;

    private JLabel lbl_titulo, lbl_1, lbl_2, lbl_3, lbl_4, lbl_5, lbl_6;
    private JTextField txt_id, txt_hora_inicio, txt_hora_fin;
    private JComboBox cbo_dia, cbo_curso, cbo_docente, cbo_aula;
    private JButton btn_nuevo, btn_agregar, btn_editar, btn_borrar, btn_cerrar;
    private JTable tb_horario;
    private JScrollPane scr_horario;

    private final CRUDHorario crud_horario = new CRUDHorario();
    private ArrayList<String[]> arr_curso;
    private ArrayList<String[]> arr_docente;
    private ArrayList<String[]> arr_aula;
    private final ValidacionController validacion =
            new ValidacionController();

    public AppHorario() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        LimpiarDatos();
        MostrarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("Instituto San José - HORARIO");
        this.setSize(560, 560);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ConfigurarControles() {
        lbl_titulo = new JLabel("HORARIOS");
        lbl_titulo.setFont(new Font("Calibri", Font.BOLD, 24));
        lbl_titulo.setForeground(Color.BLUE);
        lbl_titulo.setBounds(10, 15, 300, 25);

        lbl_1 = new JLabel("Día");
        lbl_1.setBounds(20, 60, 100, 25);
        cbo_dia = new JComboBox(new String[]{"Seleccione día", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"});
        cbo_dia.setBounds(130, 60, 150, 25);

        lbl_2 = new JLabel("Hora Inicio");
        lbl_2.setBounds(20, 100, 100, 25);
        txt_hora_inicio = new JTextField("08:00:00");
        txt_hora_inicio.setHorizontalAlignment(JTextField.CENTER);
        txt_hora_inicio.setBounds(130, 100, 100, 25);

        lbl_3 = new JLabel("Hora Fin");
        lbl_3.setBounds(250, 100, 70, 25);
        txt_hora_fin = new JTextField("10:00:00");
        txt_hora_fin.setHorizontalAlignment(JTextField.CENTER);
        txt_hora_fin.setBounds(330, 100, 100, 25);

        lbl_4 = new JLabel("Curso");
        lbl_4.setBounds(20, 140, 100, 25);
        cbo_curso = new JComboBox();
        cbo_curso.setBounds(130, 140, 400, 25);

        arr_curso = this.ObtenerCursos();
        for (String[] c : arr_curso)
            cbo_curso.addItem(c[1]);

        lbl_5 = new JLabel("Docente");
        lbl_5.setBounds(20, 180, 100, 25);
        cbo_docente = new JComboBox();
        cbo_docente.setBounds(130, 180, 400, 25);

        arr_docente = this.ObtenerDocentes();
        for (String[] d : arr_docente)
            cbo_docente.addItem(d[1]);

        lbl_6 = new JLabel("Aula");
        lbl_6.setBounds(20, 220, 100, 25);
        cbo_aula = new JComboBox();
        cbo_aula.setBounds(130, 220, 400, 25);

        arr_aula = this.ObtenerAulas();
        for (String[] a : arr_aula)
            cbo_aula.addItem(a[1]);

        txt_id = new JTextField();
        txt_id.setVisible(false);

        btn_nuevo = new JButton("NUEVO");
        btn_nuevo.setBounds(10, 260, 90, 25);
        btn_nuevo.addActionListener(this);

        btn_agregar = new JButton("AGREGAR");
        btn_agregar.setBounds(120, 260, 90, 25);
        btn_agregar.addActionListener(this);

        btn_editar = new JButton("EDITAR");
        btn_editar.setBounds(230, 260, 90, 25);
        btn_editar.addActionListener(this);

        btn_borrar = new JButton("BORRAR");
        btn_borrar.setBounds(340, 260, 90, 25);
        btn_borrar.addActionListener(this);

        btn_cerrar = new JButton("CERRAR");
        btn_cerrar.setFont(new Font("Consolas", Font.BOLD, 14));
        btn_cerrar.setBackground(Color.RED);
        btn_cerrar.setForeground(Color.WHITE);
        btn_cerrar.setBounds(180, 480, 180, 30);
        btn_cerrar.addActionListener(this);

        tb_horario = new JTable();
        tb_horario.setRowHeight(20);
        tb_horario.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        scr_horario = new JScrollPane(tb_horario);
        scr_horario.setBounds(10, 300, 530, 160);

        this.add(lbl_titulo);
        this.add(lbl_1);
        this.add(cbo_dia);
        this.add(lbl_2);
        this.add(txt_hora_inicio);
        this.add(lbl_3);
        this.add(txt_hora_fin);
        this.add(lbl_4);
        this.add(cbo_curso);
        this.add(lbl_5);
        this.add(cbo_docente);
        this.add(lbl_6);
        this.add(cbo_aula);
        this.add(txt_id);
        this.add(btn_nuevo);
        this.add(btn_agregar);
        this.add(btn_editar);
        this.add(btn_borrar);
        this.add(btn_cerrar);
        this.add(scr_horario);

        ControladorClick click = new ControladorClick();
        tb_horario.addMouseListener(click);
    }

    // SE AGREGA MÉTODO PARA CONTROLAR ESTADO DE BOTONES
    private void HabilitarBotones(boolean nuevo, boolean agregar, boolean editar, boolean borrar) {
        btn_nuevo.setEnabled(nuevo);
        btn_agregar.setEnabled(agregar);
        btn_editar.setEnabled(editar);
        btn_borrar.setEnabled(borrar);
    }

    private ArrayList<String[]> ObtenerCursos() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione curso"});
        try (ResultSet rs = crud_horario.getListarCursos()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_curso"), rs.getString("nombre") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cursos: " + e.getMessage());
        }
        return arr_lista;
    }

    private ArrayList<String[]> ObtenerDocentes() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione docente"});
        try (ResultSet rs = crud_horario.getListarDocentes()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_usuario"), rs.getString("docente") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener docentes: " + e.getMessage());
        }
        return arr_lista;
    }

    private ArrayList<String[]> ObtenerAulas() {
        ArrayList<String[]> arr_lista = new ArrayList<>();
        arr_lista.add(new String[]{"", "Seleccione aula"});
        try (ResultSet rs = crud_horario.getListarAulas()) {
            while (rs.next()) {
                arr_lista.add(new String[]{ rs.getString("id_aula"), rs.getString("nombre") });
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener aulas: " + e.getMessage());
        }
        return arr_lista;
    }

    private void MostrarDatos() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        try (ResultSet rs = crud_horario.getListar()) {
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
            System.err.println("Error para cargar horarios: " + e.getMessage());
        }

        tb_horario.setModel(modelo);
        tb_horario.setRowHeight(22);

        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumnModel columnas = tb_horario.getColumnModel();
        if (columnas.getColumnCount() >= 6) {
            for (int i = 0; i < 4; i++)
                columnas.getColumn(i).setCellRenderer(centro);
        }
    }

    private class ControladorClick extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int fila = tb_horario.getSelectedRow();
            if (fila < 0) return;

            // SE HABILITAN EDITAR Y BORRAR, SE DESHABILITA AGREGAR
            HabilitarBotones(true, false, true, true);

            String id = tb_horario.getValueAt(fila, 0).toString();
            try {
                ResultSet rs = crud_horario.getBuscar(id);
                rs.next();
                txt_id.setText(String.valueOf(rs.getInt("id_horario")));
                cbo_dia.setSelectedItem(rs.getString("dia"));
                txt_hora_inicio.setText(rs.getString("hora_inicio"));
                txt_hora_fin.setText(rs.getString("hora_fin"));

                int idCur = rs.getInt("id_curso");
                for (int i = 0; i < arr_curso.size(); i++) {
                    if (arr_curso.get(i)[0].equals(String.valueOf(idCur))) {
                        cbo_curso.setSelectedIndex(i);
                        break;
                    }
                }

                int idDoc = rs.getInt("id_docente");
                for (int i = 0; i < arr_docente.size(); i++) {
                    if (arr_docente.get(i)[0].equals(String.valueOf(idDoc))) {
                        cbo_docente.setSelectedIndex(i);
                        break;
                    }
                }

                int idAul = rs.getInt("id_aula");
                for (int i = 0; i < arr_aula.size(); i++) {
                    if (arr_aula.get(i)[0].equals(String.valueOf(idAul))) {
                        cbo_aula.setSelectedIndex(i);
                        break;
                    }
                }
            } catch (SQLException ex) {
                System.err.println("Error al seleccionar horario: " + ex.getMessage());
            }
        }
    }
    
    private boolean ValidarFormulario() {

        if (cbo_dia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un día");
            cbo_dia.requestFocus();
            return false;
        }

        String hi = txt_hora_inicio.getText().trim();
        String hf = txt_hora_fin.getText().trim();

        String formatoHora = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

        if (!hi.matches(formatoHora)) {
            JOptionPane.showMessageDialog(this,
                    "Hora de inicio inválida. Use HH:MM:SS");
            txt_hora_inicio.requestFocus();
            return false;
        }

        if (!hf.matches(formatoHora)) {
            JOptionPane.showMessageDialog(this,
                    "Hora de fin inválida. Use HH:MM:SS");
            txt_hora_fin.requestFocus();
            return false;
        }

        Time horaInicio = Time.valueOf(hi);
        Time horaFin = Time.valueOf(hf);

        if (!horaInicio.before(horaFin)) {
            JOptionPane.showMessageDialog(this,
                    "La hora de inicio debe ser menor que la hora de fin");
            txt_hora_inicio.requestFocus();
            return false;
        }

        if (cbo_curso.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un curso");
            cbo_curso.requestFocus();
            return false;
        }

        if (cbo_docente.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un docente");
            cbo_docente.requestFocus();
            return false;
        }

        if (cbo_aula.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un aula");
            cbo_aula.requestFocus();
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
                JOptionPane.showMessageDialog(this,
                        "Seleccione un horario de la tabla");
                return;
            }

            int opc = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro de borrar el registro?",
                    "SENATI",
                    JOptionPane.YES_NO_OPTION
            );

            if (opc == JOptionPane.YES_OPTION) {
                crud_horario.BorrarPorId(
                        Integer.parseInt(txt_id.getText())
                );

                JOptionPane.showMessageDialog(this, "Horario eliminado");

                MostrarDatos();
                LimpiarDatos();
            }

            return;
        }

        if (e.getSource() != btn_agregar &&
                e.getSource() != btn_editar) {
            return;
        }

        if (!ValidarFormulario()) {
            return;
        }

        Time tHi = Time.valueOf(txt_hora_inicio.getText().trim());
        Time tHf = Time.valueOf(txt_hora_fin.getText().trim());

        Horario horario = new Horario();

        horario.setDia(cbo_dia.getSelectedItem().toString());
        horario.setHoraInicio(tHi);
        horario.setHoraFin(tHf);

        horario.setIdCurso(Integer.parseInt(
                arr_curso.get(cbo_curso.getSelectedIndex())[0]
        ));

        horario.setIdDocente(Integer.parseInt(
                arr_docente.get(cbo_docente.getSelectedIndex())[0]
        ));

        horario.setIdAula(Integer.parseInt(
                arr_aula.get(cbo_aula.getSelectedIndex())[0]
        ));

        if (e.getSource() == btn_agregar) {

            crud_horario.Registrar(horario);

            JOptionPane.showMessageDialog(this, "Horario registrado");

        } else if (e.getSource() == btn_editar) {

            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un horario de la tabla");
                return;
            }

            horario.setIdHorario(Integer.parseInt(txt_id.getText()));

            crud_horario.EditarPorId(horario);

            JOptionPane.showMessageDialog(this, "Horario actualizado");
        }

        MostrarDatos();
        LimpiarDatos();
    }

    private void LimpiarDatos() {
        tb_horario.clearSelection();
        txt_id.setText("");
        cbo_dia.setSelectedIndex(0);
        txt_hora_inicio.setText("08:00:00");
        txt_hora_fin.setText("10:00:00");
        cbo_curso.setSelectedIndex(0);
        cbo_docente.setSelectedIndex(0);
        cbo_aula.setSelectedIndex(0);
        // SE RESTABLECE ESTADO INICIAL DE BOTONES
        HabilitarBotones(true, true, false, false);
    }

    public static AppHorario getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppHorario();
        }
        return instancia;
    }
}