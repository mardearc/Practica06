package model;

import java.awt.*;
import java.util.Random;

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
        spriteGlobo = Toolkit.getDefaultToolkit().getImage("globo.png");
        spriteExplosion = Toolkit.getDefaultToolkit().getImage("explosion.png");
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
            g.drawImage(spriteExplosion, x, y, ancho, alto, null);
        } else {
            g.drawImage(spriteGlobo, x, y, ancho, alto, null);
        }
    }

    public void explotar() {
        explotado = true;
    }

    public void frenar() {
        velocidad = Math.max(1, velocidad - 2);
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
}

