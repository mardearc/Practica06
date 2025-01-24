package model;

import java.awt.*;

public class Techo {
    private int x, y, ancho, altura;

    public Techo(int x, int y, int ancho, int altura) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.altura = altura;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, ancho, altura);
    }

    public int getAltura() {
        return y + altura;
    }
}
