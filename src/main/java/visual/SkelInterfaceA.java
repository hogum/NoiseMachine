import javax.swing.*;
import java.awt.event.*;

public class SkelInterfaceA implements ActionListener {

    JButton button;

    public static void main (String[] args) {
        SkelInterfaceA gui = new SkelInterfaceA();
        gui.go();
    }

    public void go() {
        JFrame frame = new JFrame();
        button = new JButton("looks clickable");

        button.addActionListener(this);

        frame.getContentPane().add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    public void actionPerfomed (ActionEvent event) {
        button.setText("You sure click nice");
    }
}