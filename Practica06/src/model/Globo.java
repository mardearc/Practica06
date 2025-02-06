package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Timer;

public class Globo extends Thread {
    private int x, y, ancho, alto;
    private int id;
    private boolean finalizado;
    private boolean explotado;
    private boolean golpeado;
    private Random random;
    private int velocidad;
    private Image spriteGlobo;
    private Image spriteExplosion;
    private Image spriteGolpeado;
    private long tiempoFinalizacion;

    


    public Globo(int x, int y, int ancho, int alto, int id) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.id = id;
        this.finalizado = false;
        this.explotado = false;
        this.golpeado = false; // Inicialmente no está golpeado
        this.velocidad = 5;
        this.random = new Random();
        
        // Cargar sprites
        try {
            File archivoGlobo = new File("images/globo.png");
            File archivoExplosion = new File("images/explosion.png");
            File archivoGolpeado = new File("images/globogolpeado.png"); 
            
            BufferedImage imagenGlobo = ImageIO.read(archivoGlobo);
            BufferedImage imagenExplosion = ImageIO.read(archivoExplosion);
            BufferedImage imagenGolpeado = ImageIO.read(archivoGolpeado); 
            
            spriteGlobo = imagenGlobo;
            spriteExplosion = imagenExplosion;
            spriteGolpeado = imagenGolpeado; 
        } catch (IOException e) {
            System.err.println("Error: No se pudo cargar una o más imágenes. Verifica las rutas.");
            e.printStackTrace();
        }
    }
    // Movimiento de los globos
    public void run() {
        while (!finalizado) {
            y -= velocidad;
            x += random.nextInt(5) - 2;  //Movimiento lateral

            if (y <= 0) y = 0;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void dibujar(Graphics g) {
        if (explotado) {
            // Dibuja la imagen de explosión si el globo ha explotado
            g.drawImage(spriteExplosion, x, y, ancho, alto, null);
        } else if (golpeado) {
            // Dibuja la imagen golpeada si el globo fue golpeado
            g.drawImage(spriteGolpeado, x, y, ancho, alto, null);
        } else {
            // Dibuja la imagen normal del globo
            g.drawImage(spriteGlobo, x, y, ancho, alto, null);
        }
    }


    public void explotar() {
        explotado = true;
    }

    public void frenar() {
        if (!explotado && !golpeado) {
            // Marca el globo como golpeado
            golpeado = true;

            // Configura un temporizador para restablecer el estado después de 1000ms
            Timer timer = new Timer(1000, e -> {
                golpeado = false; // Vuelve al estado normal después de 1 segundo
            });
            timer.setRepeats(false); // Solo ejecuta una vez
            timer.start();

            // Reduce la velocidad del globo
            velocidad = Math.max(1, velocidad - 2);

            // Configura otro temporizador para acelerar después de 1 segundo
            Timer aceleracionTimer = new Timer(1000, e -> {
                acelerar(); // Acelera el globo después de 1 segundo
            });
            aceleracionTimer.setRepeats(false); // Solo ejecuta una vez
            aceleracionTimer.start();
        }
    }

    // Método para acelerar el globo después de que haya sido frenado
    public void acelerar() {
    	if(velocidad < 10) {
    		velocidad = Math.max(1, velocidad + 2);
    	}
        
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + ancho && mouseY >= y && mouseY <= y + alto;
    }

    public int getY() {
        return y;
    }

    public int getGloboId() {
        return id;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
    
    public long getTiempoFinalizacion() {
        return tiempoFinalizacion;
    }

    public void setTiempoFinalizacion(long tiempoFinalizacion) {
        this.tiempoFinalizacion = tiempoFinalizacion;
    }
}

