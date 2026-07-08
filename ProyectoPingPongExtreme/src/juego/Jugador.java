package juego;

/**
 *
 * @author pserr
 */
public class Jugador {

    // Nombre del jugador
    private String nombre;

    // Puntaje actual de la ronda
    private int puntos;

    // Cantidad de rondas ganadas
    private int rondasGanadas;

    /**
     * Constructor
     * Se ejecuta cuando se crea un jugador nuevo.
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntos = 0;
        this.rondasGanadas = 0;
    }

    //getter
    
    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getRondasGanadas() {
        return rondasGanadas;
    }

    //setter
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setRondasGanadas(int rondasGanadas) {
        this.rondasGanadas = rondasGanadas;
    }

    //metodos
    
    /**
     * Suma puntos al jugador.
     */
    public void sumarPuntos(int cantidad) {
        puntos += cantidad;
    }

    /**
     * Resta puntos.
     * Evita que el puntaje sea menor que cero.
     */
    public void restarPuntos(int cantidad) {
        puntos -= cantidad;

        if (puntos < 0) {
            puntos = 0;
        }
    }

    /**
     * Reinicia los puntos al comenzar una nueva ronda.
     */
    public void reiniciarPuntos() {
        puntos = 0;
    }

    /**
     * Aumenta una ronda ganada.
     */
    public void ganarRonda() {
        rondasGanadas++;
    }

    /**
     * Reinicia completamente al jugador.
     */
    public void reiniciarJugador() {
        puntos = 0;
        rondasGanadas = 0;
    }

    /**
     * Devuelve toda la información del jugador.
     */
    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", puntos=" + puntos +
                ", rondasGanadas=" + rondasGanadas +
                '}';
    }
}