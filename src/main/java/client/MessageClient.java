// Version 1 // Send only

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.PrintWriter;

import java.net.Socket;

/*

    Reads text from a text field
    and sends it to a socket.

*/

public class MessageClient {

    JTextField outgoingMessage;
    PrintWriter printer;
    Socket socket;

    public void start() {

        JFrame frame = new JFrame("Might be a Messaging Client");
        JPanel panel = new JPanel();
        outgoingMessage =  new JTextField(20);
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(new SendButtonListener());

        panel.add(outgoingMessage);
        panel.add(sendButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(400, 500);
        frame.setVisible(true);
    }

    public class SendButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ev) {


        }
    }

    public static void main(String[] args) {
        
        MessageClient mc = new MessageClient();
        mc.start();
    }
}