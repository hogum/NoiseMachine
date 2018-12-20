// Version 1 // Send only

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.PrintWriter;
import java.io.IOException;

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

        setConnection();

        sendButton.addActionListener(new SendButtonListener());

        panel.add(outgoingMessage);
        panel.add(sendButton);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setBounds(350, 150, 400, 200);
        frame.setVisible(true);
    }


    private void setConnection() {

        try {

            socket = new Socket("127.0.0.1", 5880);
            printer = new PrintWriter(socket.getOutputStream());
            System.out.println("Connection Established!");
        
        } catch (IOException ex) {

            System.out.println("Oops! That's not good\n" + ex);
            ex.printStackTrace();
        }
    }


    class SendButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ev) {

            try {

                    printer.println(outgoingMessage.getText());
                    printer.flush();
            
            } catch(Exception ex) {
                ex.printStackTrace();
            }

            outgoingMessage.setText("");
            outgoingMessage.requestFocus();
        }
    }

    public static void main(String[] args) {
        
        MessageClient mc = new MessageClient();
        mc.start();
    }
}