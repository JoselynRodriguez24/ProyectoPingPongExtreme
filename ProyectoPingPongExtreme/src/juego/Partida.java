package juego;

/**
 * @author pserr
 */
public class Partida {

    // Jugadores de la partida
    private final Jugador jugador1;
    private final Jugador jugador2;

    // Control de rondas
    private int rondaActual;
    private final int TOTAL_RONDAS = 3;

    // Constructor
    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.rondaActual = 1;
    }

    // Devuelve el jugador 1
    public Jugador getJugador1() {
        return jugador1;
    }

    // Devuelve el jugador 2
    public Jugador getJugador2() {
        return jugador2;
    }

    // Devuelve la ronda actual
    public int getRondaActual() {
        return rondaActual;
    }

    // Devuelve el total de rondas
    public int getTotalRondas() {
        return TOTAL_RONDAS;
    }

    // Avanza a la siguiente ronda
    public void siguienteRonda() {
        if (rondaActual < TOTAL_RONDAS) {
            rondaActual++;
        }
    }

    // Determina el ganador de la ronda
    public void finalizarRonda() {

        if (jugador1.getPuntos() > jugador2.getPuntos()) {
            jugador1.ganarRonda();
        } else if (jugador2.getPuntos() > jugador1.getPuntos()) {
            jugador2.ganarRonda();
        }

        // Reinicia los puntos para la siguiente ronda
        jugador1.reiniciarPuntos();
        jugador2.reiniciarPuntos();
    }

    // Verifica si la partida termino
    public boolean partidaTerminada() {
        return jugador1.getRondasGanadas() == 2
                || jugador2.getRondasGanadas() == 2;
    }

    // Devuelve el jugador ganador
    public Jugador obtenerGanador() {

        if (jugador1.getRondasGanadas() > jugador2.getRondasGanadas()) {
            return jugador1;
        }

        return jugador2;
    }
}