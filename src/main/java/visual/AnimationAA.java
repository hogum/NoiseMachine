import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Color;

public class AnimationAA {

    int x = 70;
    int y = 70;

    public static void main (String[] args) {
        AnimationAA gui = new AnimationAA();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MyDrawPanel panel = new MyDrawPanel();

        frame.getContentPane().add(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);


        for (int i=0; i < 300; i++) {
            x++;
            y++;

            panel.repaint();

            try {
                Thread.sleep(50);
            } catch (Exception ex) {

            }
        }
    }

    class MyDrawPanel extends JPanel {
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(Color.blue);
            g.fillOval(x, y, 40, 40);
        }
    }
}