/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pingpong;

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Paleta de colores y componentes reutilizables para dar una apariencia
 * mas profesional (estilo "arcade") a toda la aplicacion.
 *
 * Responsable: Integrante 1.
 *
 * No contiene logica de juego, solo constantes visuales y un JButton
 * redondeado personalizado, para que toda la interfaz use el mismo tema.
 */
public final class EstiloVisual {

    private EstiloVisual() {}

    // Paleta general (tema oscuro tipo arcade)
    public static final Color FONDO_PRINCIPAL = new Color(18, 21, 36);      // azul muy oscuro
    public static final Color FONDO_PANEL = new Color(27, 31, 59);         // panel superior/inferior
    public static final Color FONDO_CANCHA = new Color(8, 10, 20);         // negro azulado

    public static final Color ACENTO_JUGADOR1 = new Color(255, 107, 53);   // naranja
    public static final Color ACENTO_JUGADOR2 = new Color(0, 217, 255);    // cian
    public static final Color ACENTO_NEUTRO = new Color(120, 220, 120);    // verde suave (boton iniciar)
    public static final Color ACENTO_ALERTA = new Color(255, 196, 0);      // amarillo (pausar)
    public static final Color ACENTO_PELIGRO = new Color(255, 82, 82);     // rojo (reiniciar)

    public static final Color TEXTO_CLARO = new Color(235, 238, 245);
    public static final Color TEXTO_SECUNDARIO = new Color(160, 168, 190);

    public static final Font FUENTE_TITULO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FUENTE_MARCADOR = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FUENTE_TEMPORIZADOR = new Font("Monospaced", Font.BOLD, 26);
    public static final Font FUENTE_SECUNDARIA = new Font("SansSerif", Font.PLAIN, 13);

    /** Boton redondeado con color de acento configurable, sin el rectangulo feo por defecto de Swing. */
    public static class BotonRedondeado extends JButton {
        private final Color colorBase;

        public BotonRedondeado(String texto, Color colorBase) {
            super(texto);
            this.colorBase = colorBase;
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(10, 26, 10, 26));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color color = isEnabled() ? colorBase : new Color(90, 95, 110);
            if (getModel().isPressed()) {
                color = color.darker();
            } else if (getModel().isRollover()) {
                color = color.brighter();
            }

            g2d.setColor(color);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 18, 18));
            g2d.dispose();

            super.paintComponent(g);
        }
    }

    /** Panel simple con esquinas redondeadas, usado como "badge" del marcador. */
    public static class PanelRedondeado extends JPanel {
        private final Color colorFondo;
        private final int radio;

        public PanelRedondeado(Color colorFondo, int radio) {
            this.colorFondo = colorFondo;
            this.radio = radio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(colorFondo);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radio, radio));
            g2d.dispose();
            super.paintComponent(g);
        }
    }
}