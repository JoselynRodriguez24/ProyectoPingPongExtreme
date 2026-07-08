/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;

/**
 *
 * @author pserr
 */
public class Partida {
    
    // Jugadores de la partida
    private final Jugador jugador1;
    private final Jugador jugador2;
    
    // Control de rondas
    private int rondaActual;
    private final int TOTAL_RONDAS = 3;
    
    //Constructor
    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.rondaActual = 1;
    }
    
    //getter
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
    
    //Avanza a la siguiente ronda
    public void siguienteRonda() {
        if (rondaActual < TOTAL_RONDAS) {
            rondaActual++;
        }
    }

    //Determina el ganador de la ronda y reinicia los puntos
    public void finalizarRonda() {

        if (jugador1.getPuntos() > jugador2.getPuntos()) {
            jugador1.ganarRonda();
        } else if (jugador2.getPuntos() > jugador1.getPuntos()) {
            jugador2.ganarRonda();
        }

        jugador1.reiniciarPuntos();
        jugador2.reiniciarPuntos();
    }
    
    //verifica si la partida termino
    public boolean partidaTerminada() {
        return jugador1.getRondasGanadas() == 2
                || jugador2.getRondasGanadas() == 2;
    }

    //se obtiene el ganador
    public Jugador obtenerGanador() {

        if (jugador1.getRondasGanadas() > jugador2.getRondasGanadas()) {
            return jugador1;
        }

        return jugador2;
    }
}