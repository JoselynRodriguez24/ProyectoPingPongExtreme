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
 * Pantalla inicial (JDialog modal) que se muestra antes de abrir el juego.
 *
 * Responsable: Integrante 1.
 *
 * Captura:
 *  - Nombre del jugador izquierdo (controles W / S)
 *  - Nombre del jugador derecho (controles flecha arriba / flecha abajo)
 *  - Nivel de dificultad (Facil, Normal, Dificil, Extremo)
 *
 * Integrante 2 y 3 deben usar el valor devuelto por getDificultadSeleccionada()
 * para ajustar velocidad de bolas, cantidad maxima de bolas activas y
 * frecuencia de aparicion de bolas especiales (puntos 4 y 5 del enunciado).
 */
public class PantallaInicio extends JDialog {

    public static final String[] NIVELES_DIFICULTAD = {"Facil", "Normal", "Dificil", "Extremo"};

    private final JTextField campoNombreJugador1;
    private final JTextField campoNombreJugador2;
    private final JComboBox<String> comboDificultad;

    private String nombreJugador1;
    private String nombreJugador2;
    private String dificultadSeleccionada;
    private boolean confirmado;

    public PantallaInicio(Frame propietario) {
        super(propietario, "Ping Pong Multihilo Extreme - Configuracion", true);

        getContentPane().setBackground(EstiloVisual.FONDO_PRINCIPAL);
        setLayout(new BorderLayout(10, 10));
        setSize(420, 300);
        setLocationRelativeTo(propietario);
        setResizable(false);

        JLabel titulo = new JLabel("PING PONG MULTIHILO EXTREME", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        titulo.setForeground(EstiloVisual.ACENTO_JUGADOR2);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(EstiloVisual.FONDO_PRINCIPAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        campoNombreJugador1 = new JTextField("Jugador 1", 15);
        campoNombreJugador2 = new JTextField("Jugador 2", 15);
        comboDificultad = new JComboBox<>(NIVELES_DIFICULTAD);
        comboDificultad.setSelectedIndex(1); // Normal por defecto
        estilizarCampo(campoNombreJugador1, EstiloVisual.ACENTO_JUGADOR1);
        estilizarCampo(campoNombreJugador2, EstiloVisual.ACENTO_JUGADOR2);
        comboDificultad.setBackground(Color.WHITE);
        comboDificultad.setFont(EstiloVisual.FUENTE_SECUNDARIA);

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(crearEtiqueta("Jugador izquierdo (W / S):", EstiloVisual.ACENTO_JUGADOR1), gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoNombreJugador1, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(crearEtiqueta("Jugador derecho (Flechas):", EstiloVisual.ACENTO_JUGADOR2), gbc);
        gbc.gridx = 1;
        panelFormulario.add(campoNombreJugador2, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(crearEtiqueta("Dificultad:", EstiloVisual.TEXTO_CLARO), gbc);
        gbc.gridx = 1;
        panelFormulario.add(comboDificultad, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        EstiloVisual.BotonRedondeado botonContinuar =
                new EstiloVisual.BotonRedondeado("CONTINUAR", EstiloVisual.ACENTO_NEUTRO);
        botonContinuar.addActionListener(e -> confirmarDatos());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(EstiloVisual.FONDO_PRINCIPAL);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panelBoton.add(botonContinuar);
        add(panelBoton, BorderLayout.SOUTH);

        // Permite confirmar presionando Enter en cualquier campo de texto.
        getRootPane().setDefaultButton(botonContinuar);
    }

    private JLabel crearEtiqueta(String texto, Color color) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(color);
        etiqueta.setFont(EstiloVisual.FUENTE_SECUNDARIA);
        return etiqueta;
    }

    private void estilizarCampo(JTextField campo, Color colorAcento) {
        campo.setFont(EstiloVisual.FUENTE_SECUNDARIA);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorAcento, 2, true),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    }

    private void confirmarDatos() {
        String nombre1 = campoNombreJugador1.getText().trim();
        String nombre2 = campoNombreJugador2.getText().trim();

        if (nombre1.isEmpty()) nombre1 = "Jugador 1";
        if (nombre2.isEmpty()) nombre2 = "Jugador 2";

        this.nombreJugador1 = nombre1;
        this.nombreJugador2 = nombre2;
        this.dificultadSeleccionada = (String) comboDificultad.getSelectedItem();
        this.confirmado = true;

        dispose();
    }

    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public String getDificultadSeleccionada() {
        return dificultadSeleccionada;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}