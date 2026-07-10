package juego;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author nero2
 */
public class ControladorConcurrencia {
    
    
   
    private final List<Bola> bolasActivas = new CopyOnWriteArrayList<>();
    private final int maximoBolas;
    
    private final AtomicInteger puntajeJugador1 = new AtomicInteger(0);
    private final AtomicInteger puntajeJugador2 = new AtomicInteger(0);
    
    private final AtomicBoolean pausado = new AtomicBoolean(false);
            
    public ControladorConcurrencia(int maximoBolas) {
        this.maximoBolas = maximoBolas;
    }
     
    public synchronized boolean agregarBola(Bola bola) {
        
        if (bolasActivas.size() >= maximoBolas) {
            return false;
        }
        
        if (pausado.get()) {
            bola.pausar();
        }
        
        bolasActivas.add(bola);
        
        Thread hiloBola = new Thread(bola);
        hiloBola.start();
        return true;
        
    }
    
    
    public List<Bola> getBolasActivas() {
        return bolasActivas;
    }
    
    

    public int sumarPuntoJugador1(int puntos) {
        return puntajeJugador1.addAndGet(puntos);
    }

    
    public int sumarPuntoJugador2(int puntos) {
        return puntajeJugador2.addAndGet(puntos);
    }

 
    public int getPuntajeJugador1() {
        return puntajeJugador1.get();
    }
    
     public int getPuntajeJugador2() {
        return puntajeJugador2.get();
    }

    
    public void reiniciarPuntajes() {
        puntajeJugador1.set(0);
        puntajeJugador2.set(0);
    }
    

    public void pausarTodas() {
        pausado.set(true);
        for (Bola bola : bolasActivas) {
            bola.pausar();
        }
    }

 
    public void reanudarTodas() {
        pausado.set(false);
        for (Bola bola : bolasActivas) {
            bola.reanudar();
        }
    }

    public boolean isPausado() {
        return pausado.get();
    }

  
    public void detenerTodas() {
        for (Bola bola : bolasActivas) {
            bola.detener();
        }
        bolasActivas.clear();
    }
    
}
