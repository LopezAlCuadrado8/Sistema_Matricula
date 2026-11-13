package view;

import controller.EstadisticaController;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * VALOR AGREGADO: panel de estadisticas con graficos de barras dibujados
 * con Java2D (sin librerias externas).
 */
public class AppEstadisticas extends JInternalFrame {

    private static AppEstadisticas instancia = null;

    private JTabbedPane tab_graficos;
    private GraficoBarras gr_carrera, gr_bloque, gr_ingresos, gr_aulas;
    private JButton btn_actualizar, btn_cerrar;

    private final EstadisticaController ctrl = new EstadisticaController();

    public AppEstadisticas() {
        super();
        ConfigurarFormulario();
        ConfigurarControles();
        CargarDatos();
    }

    private void ConfigurarFormulario() {
        this.setTitle("ESTADÍSTICAS");
        this.setSize(680, 500);
        this.setClosable(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
    }

    private void ConfigurarControles() {
        gr_carrera = new GraficoBarras("Matriculados por carrera", "", " matr.", 0);
        gr_bloque = new GraficoBarras("Matriculados por bloque", "", " matr.", 0);
        gr_ingresos = new GraficoBarras("Ingresos por pagos, por bloque", "S/ ", "", 2);
        gr_aulas = new GraficoBarras("Uso de aulas (horas semanales asignadas)", "", " h", 1);

        tab_graficos = new JTabbedPane();
        tab_graficos.addTab("Por carrera", gr_carrera);
        tab_graficos.addTab("Por bloque", gr_bloque);
        tab_graficos.addTab("Ingresos", gr_ingresos);
        tab_graficos.addTab("Aulas", gr_aulas);

        btn_actualizar = new JButton("Actualizar");
        btn_actualizar.addActionListener(e -> CargarDatos());

        btn_cerrar = new JButton("Cerrar");
        btn_cerrar.addActionListener(e -> this.dispose());

        JPanel pnl_botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl_botones.add(btn_actualizar);
        pnl_botones.add(btn_cerrar);

        this.add(tab_graficos, BorderLayout.CENTER);
        this.add(pnl_botones, BorderLayout.SOUTH);
    }

    private void CargarDatos() {
        try {
            gr_carrera.setDatos(ctrl.matriculadosPorCarrera());
            gr_bloque.setDatos(ctrl.matriculadosPorBloque());
            gr_ingresos.setDatos(ctrl.ingresosPorBloque());
            gr_aulas.setDatos(ctrl.horasPorAula());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar las estadísticas:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static AppEstadisticas getInstancia() {
        if (instancia == null || instancia.isClosed()) {
            instancia = new AppEstadisticas();
        }
        return instancia;
    }

    // ==================================================================
    // Panel que dibuja un grafico de barras horizontales
    // ==================================================================
    public static class GraficoBarras extends JPanel {

        private static final Color[] COLORES = {
            new Color(52, 120, 198), new Color(46, 160, 120),
            new Color(230, 150, 40), new Color(190, 80, 120),
            new Color(120, 90, 190), new Color(90, 160, 200)
        };

        private final String titulo, prefijo, sufijo;
        private final int decimales;
        private Map<String, Double> datos;

        public GraficoBarras(String titulo, String prefijo, String sufijo, int decimales) {
            this.titulo = titulo;
            this.prefijo = prefijo;
            this.sufijo = sufijo;
            this.decimales = decimales;
            this.setBackground(Color.WHITE);
        }

        public void setDatos(Map<String, Double> datos) {
            this.datos = datos;
            this.repaint();
        }

        private String formatear(double v) {
            return prefijo + String.format(Locale.US, "%." + decimales + "f", v) + sufijo;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();

            g2.setFont(new Font("Calibri", Font.BOLD, 18));
            g2.setColor(new Color(30, 60, 140));
            g2.drawString(titulo, 20, 30);

            if (datos == null || datos.isEmpty()) {
                g2.setFont(new Font("Calibri", Font.PLAIN, 14));
                g2.setColor(Color.GRAY);
                g2.drawString("Sin datos para mostrar", 20, 65);
                g2.dispose();
                return;
            }

            double max = 0, total = 0;
            for (double v : datos.values()) {
                max = Math.max(max, v);
                total += v;
            }
            if (max <= 0) {
                max = 1;
            }

            g2.setFont(new Font("Calibri", Font.PLAIN, 13));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Total: " + formatear(total), 20, 52);

            FontMetrics fm = g2.getFontMetrics();
            int anchoEtiqueta = 0;
            for (String k : datos.keySet()) {
                anchoEtiqueta = Math.max(anchoEtiqueta, fm.stringWidth(k));
            }
            anchoEtiqueta = Math.min(anchoEtiqueta, w / 3);

            int xBarra = 20 + anchoEtiqueta + 10;
            int anchoMax = Math.max(40, w - xBarra - 110);
            int yInicio = 70;
            int n = datos.size();
            int paso = Math.max(24, Math.min(46, (h - yInicio - 15) / n));
            int altoBarra = (int) (paso * 0.65);

            g2.setStroke(new BasicStroke(1f));
            int i = 0;
            for (Map.Entry<String, Double> e : datos.entrySet()) {
                int y = yInicio + i * paso;
                int largo = (int) Math.round(e.getValue() / max * anchoMax);

                g2.setColor(Color.DARK_GRAY);
                String et = e.getKey();
                while (fm.stringWidth(et) > anchoEtiqueta && et.length() > 3) {
                    et = et.substring(0, et.length() - 2);
                }
                g2.drawString(et, 20, y + altoBarra - (altoBarra - fm.getAscent()) / 2);

                g2.setColor(COLORES[i % COLORES.length]);
                g2.fillRoundRect(xBarra, y, Math.max(largo, 2), altoBarra, 6, 6);

                g2.setColor(Color.DARK_GRAY);
                g2.drawString(formatear(e.getValue()), xBarra + Math.max(largo, 2) + 8,
                        y + altoBarra - (altoBarra - fm.getAscent()) / 2);
                i++;
            }
            g2.dispose();
        }
    }
}