package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.Timer;

import java.awt.*;
import java.util.Random;

public class Globo extends Thread {
    private int x, y, ancho, alto;
    private int id;
    private boolean finalizado;
    private boolean explotado;
    private Random random;
    private int velocidad;
    private Image spriteGlobo;
    private Image spriteExplosion;
    private long tiempoFinalizacion;

    


    public Globo(int x, int y, int ancho, int alto, int id) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.id = id;
        this.finalizado = false;
        this.explotado = false;
        this.velocidad = 5;
        this.random = new Random();

        // Cargar sprites
        try {
        	File archivoGlobo = new File("images/globo.png");
        	File archivoExplosion = new File("images/explosion.png");
        	try {
				BufferedImage imagenGlobo = ImageIO.read(archivoGlobo);
				BufferedImage imagenExplosion = ImageIO.read(archivoExplosion);

				spriteGlobo = imagenGlobo;
				spriteExplosion = imagenExplosion;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         } catch (NullPointerException e) {
            System.err.println("Error: No se pudo cargar la imagen. Verifica la ruta.");
        }
    }

    public void run() {
        while (!finalizado) {
            y -= velocidad;
            x += random.nextInt(3) - 1;

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
        } else {
            // Dibuja la imagen del globo si aún no ha explotado
            g.drawImage(spriteGlobo, x, y, ancho, alto, null);
        }
    }


    public void explotar() {
        explotado = true;
    }

    public void frenar() {
        velocidad = Math.max(1, velocidad - 2);

        // Crear un temporizador para esperar antes de llamar a acelerar()
        Timer timer = new Timer(1000, e -> { // Espera 1 segundo (1000 ms)
            acelerar();
        });

        timer.setRepeats(false); // Solo ejecuta una vez
        timer.start();
    }

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

