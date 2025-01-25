package model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Techo {
    private int x, y, ancho, altura;
    private Image spriteTecho;

    public Techo(int x, int y, int ancho, int altura) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.altura = altura;
        
        File archivoTecho = new File("images/techo.png");
        
        try {
			BufferedImage imagenTecho = ImageIO.read(archivoTecho);
			
			spriteTecho = imagenTecho;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void dibujar(Graphics g) {
    	g.drawImage(spriteTecho, x, y, ancho, altura, null);
    }

    public int getAltura() {
        return y + altura;
    }
}
