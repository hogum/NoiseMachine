import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SkelInterfaceB {
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
        
    }
}