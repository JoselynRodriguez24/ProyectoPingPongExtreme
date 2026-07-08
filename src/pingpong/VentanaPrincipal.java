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

/**
 * Ventana principal (JFrame) de la aplicacion.
 *
 * Responsable: Integrante 1.
 *
 * Flujo:
 *  1. Al arrancar, se muestra PantallaInicio para pedir nombres y dificultad.
 *  2. Con esos datos se construye el PanelJuego y se agrega a la ventana.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Ping Pong Multihilo Extreme");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Pantalla inicial para capturar nombres y dificultad.
        PantallaInicio pantallaInicio = new PantallaInicio(this);
        pantallaInicio.setVisible(true); // Bloquea hasta que el usuario presione "Continuar" (es modal).

        String nombreJugador1 = pantallaInicio.isConfirmado() ? pantallaInicio.getNombreJugador1() : "Jugador 1";
        String nombreJugador2 = pantallaInicio.isConfirmado() ? pantallaInicio.getNombreJugador2() : "Jugador 2";
        String dificultad = pantallaInicio.isConfirmado() ? pantallaInicio.getDificultadSeleccionada() : "Normal";

        // 2. Panel de juego con los datos capturados.
        PanelJuego panelJuego = new PanelJuego(nombreJugador1, nombreJugador2, dificultad);
        add(panelJuego, BorderLayout.CENTER);

        // El panel necesita foco para que los KeyBindings (W/S/Flechas) respondan.
        SwingUtilities.invokeLater(panelJuego::solicitarFoco);
    }
}