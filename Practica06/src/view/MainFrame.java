package view;

import javax.swing.*;

import model.Globo;
import model.Techo;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    private static final int NUM_GLOBOS = 4;

    private List<Globo> globos;
    private Techo techo;
    private boolean carreraIniciada = false;
    private JPanel panel;
    private long lastFrameTime = System.nanoTime();

    public MainFrame() {
        setTitle("Carrera de Globos");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Crear techo
        techo = new Techo(0, 0, FRAME_WIDTH, 50);

        // Crear globos
        globos = new ArrayList<>();
        for (int i = 0; i < NUM_GLOBOS; i++) {
            Globo globo = new Globo(100 + (i * 150), FRAME_HEIGHT - 100, 40, 60, i + 1);
            globos.add(globo);
        }

        // Panel principal con doble buffer
        panel = new JPanel() {
            private Image offscreenImage;
            private Graphics offscreenGraphics;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.CYAN);

                // Crear buffer si es necesario
                if (offscreenImage == null) {
                    offscreenImage = createImage(getWidth(), getHeight());
                    offscreenGraphics = offscreenImage.getGraphics();
                }

                // Dibujar en el buffer
                offscreenGraphics.clearRect(0, 0, getWidth(), getHeight());
                techo.dibujar(offscreenGraphics);
                for (Globo globo : globos) {
                    globo.dibujar(offscreenGraphics);
                }

                // Mostrar FPS
                long currentTime = System.nanoTime();
                double fps = 1_000_000_000.0 / (currentTime - lastFrameTime);
                lastFrameTime = currentTime;
                offscreenGraphics.setColor(Color.BLACK);
                offscreenGraphics.drawString(String.format("FPS: %.2f", fps), 10, 20);

                // Dibujar buffer en pantalla
                g.drawImage(offscreenImage, 0, 0, this);
            }
        };

        panel.setBounds(0, 0, 800, 450);
        getContentPane().add(panel);

        // Botón para iniciar la carrera
        JButton startButton = new JButton("Iniciar Carrera");
        startButton.setBounds(325, 500, 150, 30);
        startButton.addActionListener(e -> {
            carreraIniciada = true;
            for (Globo globo : globos) {
                globo.start();
            }
            startButton.setEnabled(false);
        });
        getContentPane().add(startButton);

        // Evento de ratón para frenar globos
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Globo globo : globos) {
                    if (globo.contains(e.getX(), e.getY())) {
                        globo.frenar();
                        break;
                    }
                }
            }
        });

        // Hilo principal para actualizar el juego
        new Thread(() -> {
            while (true) {
                if (carreraIniciada) {
                    panel.repaint();

                    // Verificar colisiones
                    for (Globo globo : globos) {
                        if (!globo.isFinalizado() && globo.getY() <= techo.getAltura()) {
                            globo.setFinalizado(true);
                            globo.explotar();
                        }
                    }

                    // Terminar si todos los globos llegan
                    if (globos.stream().allMatch(Globo::isFinalizado)) {
                        mostrarPodio();
                        break;
                    }
                }

                try {
                    Thread.sleep(15); // Control de FPS (~60 FPS)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void mostrarPodio() {
        globos.sort((g1, g2) -> Integer.compare(g2.getGloboId(), g1.getGloboId()));
        StringBuilder podio = new StringBuilder("¡Resultados de la carrera!\n");
        podio.append("Oro: Globo ").append(globos.get(0).getId()).append("\n");
        podio.append("Plata: Globo ").append(globos.get(1).getId()).append("\n");
        podio.append("Bronce: Globo ").append(globos.get(2).getId()).append("\n");

        JOptionPane.showMessageDialog(this, podio.toString(), "Podio", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

