
import javax.swing.*;

public class SkelInterface {

    public static void main (String[] args) {

        JFrame frame = new JFrame();
        JButton button = new JButton("looks clickable");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(button);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}