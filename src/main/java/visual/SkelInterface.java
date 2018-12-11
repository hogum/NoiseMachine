
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SkelInterface {

    public static void main (String[] args) {

        JFrame frame = new JFrame();
        JButton button = new JButton("looks clickable");
        Drawing p = new Drawing();
        // p.paintC(new Graphics());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(BorderLayout.NORTH, button);
        frame.setSize(600, 550);
        frame.setVisible(true);
    }

}

class Drawing extends JPanel {
    public void paintC (Graphics gp) {
        gp.setColor(Color.orange);
        gp.fillRect(20, 50, 100, 100);
    }
}