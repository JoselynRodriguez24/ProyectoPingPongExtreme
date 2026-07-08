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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel principal del area de juego.
 *
 * Responsable: Integrante 1 (Interfaz grafica y controles).
 *
 * Esta clase entrega TODO lo que pide el punto 1 y 2 del enunciado:
 *  - Area de juego (JPanel)
 *  - Nombres y puntajes visibles
 *  - Botones Iniciar / Pausar-Reanudar / Reiniciar
 *  - Temporizador visible
 *  - Captura de teclado (W/S y flechas arriba/abajo)
 *
 * IMPORTANTE - puntos de integracion con el resto del equipo:
 *
 *  - Integrante 2 (paletas): debe leer el estado del teclado con
 *    isArribaJugador1(), isAbajoJugador1(), isArribaJugador2(), isAbajoJugador2()
 *    dentro de su clase Paleta para mover las paletas y validar limites.
 *
 *  - Integrante 3 (bolas/hilos): debe dibujar sus bolas registrando un
 *    Dibujable con agregarDibujable(...). Cada hilo de bola solo necesita
 *    llamar repaint() (heredado de JPanel) despues de mover su posicion.
 *
 *  - Integrante 4 (concurrencia/puntajes): debe reemplazar los campos
 *    puntajeJugador1/puntajeJugador2 por una estructura sincronizada
 *    (AtomicInteger, por ejemplo) y usar agregarControlListener(...) para
 *    enterarse cuando el usuario presiona Iniciar / Pausar / Reiniciar y asi
 *    controlar sus hilos y su coleccion concurrente de bolas.
 */
public class PanelJuego extends JPanel {

    /** Interfaz para que Integrante 2 y 3 dibujen sus propios elementos (paletas y bolas). */
    public interface Dibujable {
        void dibujar(Graphics2D g2d, int anchoArea, int altoArea);
    }

    /** Interfaz para que el resto del equipo reaccione a los botones de control. */
    public interface ControlJuegoListener {
        default void onIniciar() {}
        default void onPausarReanudar(boolean pausado) {}
        default void onReiniciar() {}
    }

    private static final int DURACION_RONDA_SEGUNDOS = 60;

    private final String nombreJugador1;
    private final String nombreJugador2;
    private final String dificultad;

    // TODO Integrante 4: sincronizar estos puntajes (AtomicInteger / synchronized) cuando
    // varios hilos de bolas empiecen a modificarlos al mismo tiempo.
    private int puntajeJugador1 = 0;
    private int puntajeJugador2 = 0;

    private int rondasGanadasJugador1 = 0;
    private int rondasGanadasJugador2 = 0;

    private boolean partidaIniciada = false;
    private boolean partidaPausada = false;
    private int segundosRestantes = DURACION_RONDA_SEGUNDOS;

    // Estado del teclado, expuesto para que Integrante 2 mueva las paletas.
    private volatile boolean arribaJugador1 = false;
    private volatile boolean abajoJugador1 = false;
    private volatile boolean arribaJugador2 = false;
    private volatile boolean abajoJugador2 = false;

    private final List<Dibujable> dibujables = new ArrayList<>();
    private final List<ControlJuegoListener> listeners = new ArrayList<>();

    // --- Componentes de interfaz ---
    private JLabel etiquetaPuntaje1;
    private JLabel etiquetaPuntaje2;
    private JLabel etiquetaTemporizador;
    private JLabel etiquetaRondas;
    private JButton botonIniciar;
    private JButton botonPausarReanudar;
    private JButton botonReiniciar;
    private JPanel areaJuego;
    private Timer temporizadorSwing; // javax.swing.Timer: seguro para actualizar la GUI cada segundo.

    public PanelJuego(String nombreJugador1, String nombreJugador2, String dificultad) {
        this.nombreJugador1 = nombreJugador1;
        this.nombreJugador2 = nombreJugador2;
        this.dificultad = dificultad;

        setLayout(new BorderLayout());
        setBackground(EstiloVisual.FONDO_PRINCIPAL);
        construirPanelSuperior();
        construirAreaJuego();
        construirPanelInferior();
        configurarTeclado();
    }

    // ---------------------------------------------------------------
    // Construccion de interfaz
    // ---------------------------------------------------------------

    private void construirPanelSuperior() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(EstiloVisual.FONDO_PANEL);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        etiquetaPuntaje1 = new JLabel(nombreJugador1 + "  0", SwingConstants.CENTER);
        etiquetaPuntaje1.setFont(EstiloVisual.FUENTE_MARCADOR);
        etiquetaPuntaje1.setForeground(Color.WHITE);

        etiquetaPuntaje2 = new JLabel(nombreJugador2 + "  0", SwingConstants.CENTER);
        etiquetaPuntaje2.setFont(EstiloVisual.FUENTE_MARCADOR);
        etiquetaPuntaje2.setForeground(Color.WHITE);

        EstiloVisual.PanelRedondeado badgeJugador1 = new EstiloVisual.PanelRedondeado(EstiloVisual.ACENTO_JUGADOR1, 16);
        badgeJugador1.setLayout(new BorderLayout());
        badgeJugador1.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        badgeJugador1.add(etiquetaPuntaje1, BorderLayout.CENTER);

        EstiloVisual.PanelRedondeado badgeJugador2 = new EstiloVisual.PanelRedondeado(EstiloVisual.ACENTO_JUGADOR2, 16);
        badgeJugador2.setLayout(new BorderLayout());
        badgeJugador2.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        badgeJugador2.add(etiquetaPuntaje2, BorderLayout.CENTER);

        etiquetaTemporizador = new JLabel(formatearTiempo(segundosRestantes), SwingConstants.CENTER);
        etiquetaTemporizador.setFont(EstiloVisual.FUENTE_TEMPORIZADOR);
        etiquetaTemporizador.setForeground(EstiloVisual.TEXTO_CLARO);

        etiquetaRondas = new JLabel("RONDAS 0 - 0   |   DIFICULTAD: " + dificultad.toUpperCase(), SwingConstants.CENTER);
        etiquetaRondas.setFont(EstiloVisual.FUENTE_SECUNDARIA);
        etiquetaRondas.setForeground(EstiloVisual.TEXTO_SECUNDARIO);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 2));
        panelCentro.setOpaque(false);
        panelCentro.add(etiquetaTemporizador);
        panelCentro.add(etiquetaRondas);

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(badgeJugador1);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelDerecho.setOpaque(false);
        panelDerecho.add(badgeJugador2);

        panelSuperior.add(panelIzquierdo, BorderLayout.WEST);
        panelSuperior.add(panelCentro, BorderLayout.CENTER);
        panelSuperior.add(panelDerecho, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);
    }

    private void construirAreaJuego() {
        // Este panel interno es el "campo de juego". Integrante 2 y 3 dibujan aqui
        // registrando un Dibujable; esta clase solo pinta el fondo/lineas de la cancha.
        areaJuego = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarCancha((Graphics2D) g);
            }
        };
        areaJuego.setBackground(EstiloVisual.FONDO_CANCHA);
        areaJuego.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(areaJuego, BorderLayout.CENTER);
    }

    private void construirPanelInferior() {
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 14));
        panelBotones.setBackground(EstiloVisual.FONDO_PANEL);

        botonIniciar = new EstiloVisual.BotonRedondeado("INICIAR PARTIDA", EstiloVisual.ACENTO_NEUTRO);
        botonPausarReanudar = new EstiloVisual.BotonRedondeado("PAUSAR", EstiloVisual.ACENTO_ALERTA);
        botonReiniciar = new EstiloVisual.BotonRedondeado("REINICIAR", EstiloVisual.ACENTO_PELIGRO);

        botonPausarReanudar.setEnabled(false);
        botonReiniciar.setEnabled(false);

        botonIniciar.addActionListener(this::alPresionarIniciar);
        botonPausarReanudar.addActionListener(this::alPresionarPausarReanudar);
        botonReiniciar.addActionListener(this::alPresionarReiniciar);

        panelBotones.add(botonIniciar);
        panelBotones.add(botonPausarReanudar);
        panelBotones.add(botonReiniciar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void dibujarCancha(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = areaJuego.getWidth();
        int alto = areaJuego.getHeight();

        // Linea central punteada estilo neon.
        g2d.setColor(new Color(60, 70, 100));
        Stroke punteado = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{10, 10}, 0);
        g2d.setStroke(punteado);
        g2d.drawLine(ancho / 2, 10, ancho / 2, alto - 10);

        // Circulo central, solo decorativo.
        g2d.setStroke(new BasicStroke(2));
        int radio = 60;
        g2d.drawOval(ancho / 2 - radio, alto / 2 - radio, radio * 2, radio * 2);

        // Delega el dibujo de paletas y bolas a quien se haya registrado.
        for (Dibujable d : dibujables) {
            d.dibujar(g2d, ancho, alto);
        }
    }

    // ---------------------------------------------------------------
    // Botones de control
    // ---------------------------------------------------------------

    private void alPresionarIniciar(ActionEvent e) {
        partidaIniciada = true;
        partidaPausada = false;
        botonIniciar.setEnabled(false);
        botonPausarReanudar.setEnabled(true);
        botonReiniciar.setEnabled(true);

        iniciarTemporizador();
        notificarIniciar();
        solicitarFoco();
    }

    private void alPresionarPausarReanudar(ActionEvent e) {
        partidaPausada = !partidaPausada;
        botonPausarReanudar.setText(partidaPausada ? "REANUDAR" : "PAUSAR");

        if (partidaPausada) {
            temporizadorSwing.stop();
        } else {
            temporizadorSwing.start();
        }

        notificarPausarReanudar(partidaPausada);
        solicitarFoco();
    }

    private void alPresionarReiniciar(ActionEvent e) {
        partidaIniciada = false;
        partidaPausada = false;
        segundosRestantes = DURACION_RONDA_SEGUNDOS;
        puntajeJugador1 = 0;
        puntajeJugador2 = 0;
        rondasGanadasJugador1 = 0;
        rondasGanadasJugador2 = 0;

        if (temporizadorSwing != null) {
            temporizadorSwing.stop();
        }

        actualizarEtiquetasPuntaje();
        etiquetaTemporizador.setText(formatearTiempo(segundosRestantes));
        etiquetaRondas.setText("RONDAS 0 - 0   |   DIFICULTAD: " + dificultad.toUpperCase());

        botonIniciar.setEnabled(true);
        botonPausarReanudar.setEnabled(false);
        botonPausarReanudar.setText("PAUSAR");
        botonReiniciar.setEnabled(false);

        notificarReiniciar();
        areaJuego.repaint();
        solicitarFoco();
    }

    private void iniciarTemporizador() {
        temporizadorSwing = new Timer(1000, e -> {
            segundosRestantes--;
            etiquetaTemporizador.setText(formatearTiempo(segundosRestantes));
            etiquetaTemporizador.setForeground(
                    segundosRestantes <= 10 ? EstiloVisual.ACENTO_PELIGRO : EstiloVisual.TEXTO_CLARO);
            if (segundosRestantes <= 0) {
                temporizadorSwing.stop();
                // TODO Integrante 2: aqui se debe determinar el ganador de la ronda
                // comparando puntajeJugador1 vs puntajeJugador2 (punto 6 del enunciado).
            }
        });
        temporizadorSwing.start();
    }

    private String formatearTiempo(int segundos) {
        int min = segundos / 60;
        int seg = segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }

    // ---------------------------------------------------------------
    // Teclado (W/S y flechas)
    // ---------------------------------------------------------------

    private void configurarTeclado() {
        // Se usan Key Bindings (en vez de KeyListener) porque funcionan sin importar
        // que componente tenga el foco exacto dentro de la ventana.
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        registrarTecla(inputMap, actionMap, KeyEvent.VK_W, "arribaJ1_on", true, v -> arribaJugador1 = v);
        registrarTeclaLiberada(inputMap, actionMap, KeyEvent.VK_W, "arribaJ1_off", v -> arribaJugador1 = v);

        registrarTecla(inputMap, actionMap, KeyEvent.VK_S, "abajoJ1_on", true, v -> abajoJugador1 = v);
        registrarTeclaLiberada(inputMap, actionMap, KeyEvent.VK_S, "abajoJ1_off", v -> abajoJugador1 = v);

        registrarTecla(inputMap, actionMap, KeyEvent.VK_UP, "arribaJ2_on", true, v -> arribaJugador2 = v);
        registrarTeclaLiberada(inputMap, actionMap, KeyEvent.VK_UP, "arribaJ2_off", v -> arribaJugador2 = v);

        registrarTecla(inputMap, actionMap, KeyEvent.VK_DOWN, "abajoJ2_on", true, v -> abajoJugador2 = v);
        registrarTeclaLiberada(inputMap, actionMap, KeyEvent.VK_DOWN, "abajoJ2_off", v -> abajoJugador2 = v);
    }

    private interface ConsumidorBooleano {
        void aceptar(boolean valor);
    }

    private void registrarTecla(InputMap inputMap, ActionMap actionMap, int keyCode, String nombreAccion,
                                 boolean valor, ConsumidorBooleano consumidor) {
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), nombreAccion);
        actionMap.put(nombreAccion, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumidor.aceptar(valor);
            }
        });
    }

    private void registrarTeclaLiberada(InputMap inputMap, ActionMap actionMap, int keyCode, String nombreAccion,
                                         ConsumidorBooleano consumidor) {
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), nombreAccion);
        actionMap.put(nombreAccion, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consumidor.aceptar(false);
            }
        });
    }

    public void solicitarFoco() {
        requestFocusInWindow();
    }

    // Metodos de lectura para que Integrante 2 mueva las paletas.
    public boolean isArribaJugador1() { return arribaJugador1; }
    public boolean isAbajoJugador1() { return abajoJugador1; }
    public boolean isArribaJugador2() { return arribaJugador2; }
    public boolean isAbajoJugador2() { return abajoJugador2; }

    // ---------------------------------------------------------------
    // Puntajes (uso temporal; Integrante 4 debe sincronizar esto)
    // ---------------------------------------------------------------

    public void sumarPuntajeJugador1(int puntos) {
        puntajeJugador1 += puntos;
        actualizarEtiquetasPuntaje();
    }

    public void sumarPuntajeJugador2(int puntos) {
        puntajeJugador2 += puntos;
        actualizarEtiquetasPuntaje();
    }

    private void actualizarEtiquetasPuntaje() {
        etiquetaPuntaje1.setText(nombreJugador1 + "  " + puntajeJugador1);
        etiquetaPuntaje2.setText(nombreJugador2 + "  " + puntajeJugador2);
    }

    // ---------------------------------------------------------------
    // Puntos de extension para el resto del equipo
    // ---------------------------------------------------------------

    /** Integrante 2 y 3: registren aqui como se dibujan paletas y bolas. */
    public void agregarDibujable(Dibujable dibujable) {
        dibujables.add(dibujable);
    }

    /** Integrante 4: escuchen aqui los eventos de Iniciar/Pausar/Reiniciar. */
    public void agregarControlListener(ControlJuegoListener listener) {
        listeners.add(listener);
    }

    /** Area de juego real (donde se deben dibujar paletas y bolas con coordenadas). */
    public JPanel getAreaJuego() {
        return areaJuego;
    }

    public boolean isPartidaIniciada() { return partidaIniciada; }
    public boolean isPartidaPausada() { return partidaPausada; }
    public String getDificultad() { return dificultad; }

    private void notificarIniciar() {
        for (ControlJuegoListener l : listeners) l.onIniciar();
    }

    private void notificarPausarReanudar(boolean pausado) {
        for (ControlJuegoListener l : listeners) l.onPausarReanudar(pausado);
    }

    private void notificarReiniciar() {
        for (ControlJuegoListener l : listeners) l.onReiniciar();
    }
}