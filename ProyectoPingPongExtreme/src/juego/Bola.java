package juego;

import java.awt.Graphics;
import juego.Paleta;

public class Bola implements Runnable {

    private int x;
    private int y;
    private int diametro;

    private int velocidadX;
    private int velocidadY;

    private TipoBola tipo;

    private boolean activa;
    private boolean fantasmaUsada; 
    private long tiempoIgnorarChoque;
    private boolean efectoCongelanteUsado;
    private boolean pausada;

    public Bola(int x, int y, int diametro,
            int velocidadX, int velocidadY,
            TipoBola tipo) {

        this.x = x;
        this.y = y;
        this.diametro = diametro;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
        this.tipo = tipo;

        
        if (tipo == TipoBola.RAPIDA) {
            this.velocidadX *= 2;
            this.velocidadY *= 2;
        }

        activa = true;
        pausada = false;
        efectoCongelanteUsado = false;
        fantasmaUsada = false;
        tiempoIgnorarChoque = 0;
    }

    @Override
    public void run() {

        while (activa) {

            if (!pausada) {
                mover();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                activa = false;
            }
        }
    }

    public void mover() {

        x += velocidadX;
        y += velocidadY;
    }

    public void rebotar(int anchoPanel, int altoPanel) {

        if (y <= 0) {
            y = 0;
            velocidadY = Math.abs(velocidadY);
        }

        if (y + diametro >= altoPanel) {
            y = altoPanel - diametro;
            velocidadY = -Math.abs(velocidadY);
        }
    }

    public boolean salioPorIzquierda() {
        return x + diametro < 0;
    }

    public boolean salioPorDerecha(int anchoPanel) {
        return x > anchoPanel;
    }

    public void verificarChoquePaleta(Paleta paleta) {

    boolean choqueX = x < paleta.getX() + paleta.getAncho()
            && x + diametro > paleta.getX();

    boolean choqueY = y < paleta.getY() + paleta.getAlto()
            && y + diametro > paleta.getY();

    if (choqueX && choqueY) {

        if (tipo == TipoBola.FANTASMA && !fantasmaUsada) {
            fantasmaUsada = true;

            if (velocidadX > 0) {
                x = paleta.getX() + paleta.getAncho() + 2;
            } else {
                x = paleta.getX() - diametro - 2;
            }

            return;
        }

        velocidadX *= -1;
    }
}

    public void dibujar(Graphics g) {

        g.setColor(tipo.getColor());
        g.fillOval(x, y, diametro, diametro);
    }

    public void pausar() {
        pausada = true;
    }

    public void reanudar() {
        pausada = false;
    }

    public void reiniciarPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void detener() {
        activa = false;
    }

    public boolean isActiva() {
        return activa;
    }

    public TipoBola getTipo() {
        return tipo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiametro() {
        return diametro;
    }

    public int getVelocidadX() {
        return velocidadX;
    }

    public int getVelocidadY() {
        return velocidadY;
    }

    public void setVelocidadX(int velocidadX) {
        this.velocidadX = velocidadX;
    }

    public void setVelocidadY(int velocidadY) {
        this.velocidadY = velocidadY;
    }

    public void invertirDireccionX() {
        velocidadX *= -1;
    }

    public void invertirDireccionY() {
        velocidadY *= -1;
    }
    
    public boolean puedeAplicarCongelante() {
    return tipo == TipoBola.CONGELANTE && !efectoCongelanteUsado;
    }

    public void marcarCongelanteUsado() {
    efectoCongelanteUsado = true;
    }
}