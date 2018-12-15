import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.BorderLayout;

public class TextArea implements ActionListener {

    JTextArea textA;

    public static void main(String[] args) {
        
        TextArea showIt = new TextArea();
        showIt.start();
    }

    public void start() {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton button = new JButton("Whatever");
        //JScrollPane scrollPane = new JScrollPane(textA);

        button.addActionListener(this);
        textA = new JTextArea(12, 25);
        textA.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(textA);

        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel.add(scrollPane);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.SOUTH, button);

        frame.setSize(350, 300);
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent event) {
        textA.append("Clicked me\n");
    }
}