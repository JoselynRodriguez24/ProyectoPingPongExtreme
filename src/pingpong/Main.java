/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pingpong;

/**
 *
 * @author HP
 */
import javax.swing.SwingUtilities;

/**
 * Punto de entrada de la aplicacion "Ping Pong Multihilo Extreme".
 *
 * Responsable: Integrante 1 (Interfaz grafica y controles).
 *
 * Nota para el equipo: toda la logica de juego (paletas, bolas, hilos,
 * concurrencia) se conecta desde PanelJuego. Los demas integrantes deben
 * mirar los metodos marcados con "TODO Integrante X" dentro de PanelJuego.
 */
public class Main {
    public static void main(String[] args) {
        // Las aplicaciones Swing siempre deben construirse en el Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}