package juego;

import java.awt.Color;

public enum TipoBola {

    NORMAL(Color.WHITE, 1),
    NEGATIVA(Color.RED, -2),
    BONUS(Color.BLUE, 2),
    RAPIDA(Color.YELLOW, 1),
    FANTASMA(new Color(160, 70, 255), 1),
    CONGELANTE(Color.CYAN, 1);

    private final Color color;
    private final int puntos;

    TipoBola(Color color, int puntos) {
        this.color = color;
        this.puntos = puntos;
    }

    public Color getColor() {
        return color;
    }

    public int getPuntos() {
        return puntos;
    }
}
