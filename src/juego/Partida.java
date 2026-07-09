package juego;

/**
 *
 * @author pserr
 */
public class Partida {

    private final Jugador jugador1;
    private final Jugador jugador2;

    private int rondaActual;
    private final int TOTAL_RONDAS = 3;

    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.rondaActual = 1;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public int getRondaActual() {
        return rondaActual;
    }

    public int getTotalRondas() {
        return TOTAL_RONDAS;
    }

    public void siguienteRonda() {
        if (rondaActual < TOTAL_RONDAS) {
            rondaActual++;
        }
    }

    public void finalizarRonda() {

        if (jugador1.getPuntos() > jugador2.getPuntos()) {
            jugador1.ganarRonda();
        } else if (jugador2.getPuntos() > jugador1.getPuntos()) {
            jugador2.ganarRonda();
        }

        jugador1.reiniciarPuntos();
        jugador2.reiniciarPuntos();
    }

    public boolean partidaTerminada() {
        return jugador1.getRondasGanadas() == 2
                || jugador2.getRondasGanadas() == 2;
    }

    public Jugador obtenerGanador() {

        if (jugador1.getRondasGanadas() > jugador2.getRondasGanadas()) {
            return jugador1;
        }

        return jugador2;
    }
}
