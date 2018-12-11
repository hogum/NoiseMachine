import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SkelInterfaceB implements ActionListener {

    JFrame frame;

    public static void main (String[] args) {

        SkelInterfaceB gui = new SkelInterfaceB();
        gui.go();
    }

    public void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton button = new JButton("Play with Colours");
        button.addActionListener(this);

        MyDrawPanel drawPanel = new MyDrawPanel();

        frame.getContentPane().add(BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setSize(400, 450);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {

        frame.repaint();
    }
}

class MyDrawPanel extends JPanel {
    public void paintComponent (Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        Color startColour = new Color(red, green, blue);

        red = (int) (Math.random() * 255);
        green = (int) (Math.random() * 255);
        blue = (int) (Math.random() * 255);
        Color endColour = new Color(red, green, blue);

        GradientPaint gradient = new GradientPaint(70, 70, startColour, 150, 150, endColour);
        g2d.setPaint(gradient);
        g2d.fillOval(70, 70, 100, 100);
    }
}