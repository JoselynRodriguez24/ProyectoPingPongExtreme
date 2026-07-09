package juego;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author pserr
 */
public class Paleta {

    // Posición de la paleta
    private int x;
    private int y;

    // Tamaño
    private int ancho;
    private int alto;

    // Velocidad de movimiento
    private int velocidad;

    // Color de la paleta
    private Color color;

    /**
     * Constructor
     */
    public Paleta(int x, int y, int ancho, int alto, int velocidad, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
        this.color = color;
    }

    // ==========================
    // GETTERS
    // ==========================

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public Color getColor() {
        return color;
    }

    // ==========================
    // SETTERS
    // ==========================

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // ==========================
    // MOVIMIENTO
    // ==========================

    /**
     * Mueve la paleta hacia arriba.
     */
    public void subir() {
        y -= velocidad;
    }

    /**
     * Mueve la paleta hacia abajo.
     */
    public void bajar() {
        y += velocidad;
    }

    /**
     * Evita que la paleta salga del área de juego.
     *
     * @param alturaPanel Altura del panel donde se juega.
     */
    public void validarLimites(int alturaPanel) {

        // Límite superior
        if (y < 0) {
            y = 0;
        }

        // Límite inferior
        if (y + alto > alturaPanel) {
            y = alturaPanel - alto;
        }
    }

    /**
     * Dibuja la paleta en el panel.
     */
    public void dibujar(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, ancho, alto);
    }

    /**
     * Reinicia la posición de la paleta.
     */
    public void reiniciar(int nuevaY) {
        this.y = nuevaY;
    }

    @Override
    public String toString() {
        return "Paleta{" +
                "x=" + x +
                ", y=" + y +
                ", ancho=" + ancho +
                ", alto=" + alto +
                ", velocidad=" + velocidad +
                '}';
    }

    public void cambiarVelocidadTemporal(int nuevaVelocidad, int milisegundos) {

    int velocidadOriginal = velocidad;

    velocidad = nuevaVelocidad;

    new Thread(() -> {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
        }

        velocidad = velocidadOriginal;

    }).start();
    }
}
