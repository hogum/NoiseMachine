// Version 2 // Send and receive

import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.Socket;

/*

    Reads text from a text field, sends it to a socket
    and receives texts from socket.

*/


public class MessageClientV2 {

    JTextField outgoingMessage;
    JTextArea incomingMessages;

    PrintWriter printer;
    BufferedReader bufR;
    Socket socket;

    public void start() {

        JFrame frame = new JFrame("Might be a Messaging Client");
        JPanel panel = new JPanel();
        outgoingMessage =  new JTextField(20);
        JButton sendButton = new JButton("Send");

        incomingMessages = new JTextArea(15, 20);
        incomingMessages.setLineWrap(true);
        incomingMessages.setWrapStyleWord(true);
        incomingMessages.setEditable(false);

        JScrollPane incScroller = new JScrollPane();
        incScroller.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        setConnection();

        sendButton.addActionListener(new SendButtonListener());

        panel.add(outgoingMessage);
        panel.add(sendButton);
        panel.add(incScroller);

        Thread readerThread = new Thread(new IncomingReader(), "Incoming Reader");
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setBounds(350, 150, 500, 300);
        frame.setVisible(true);
    }

    private void setConnection() {

        try {

            socket = new Socket("127.0.0.1", 5880);
            printer = new PrintWriter(socket.getOutputStream());
            InputStreamReader streamR = new InputStreamReader(socket.getInputStream());
            bufR = new BufferedReader(streamR);
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

    public class IncomingReader implements Runnable {

        public void run() {

            String text;

            try {
                    while((text = bufR.readLine()) != null) {

                        System.out.println("Read    " + text);
                        incomingMessages.append(text + '\n');
                    }

            } catch(Exception ex) {

                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        
        MessageClientV2 m = new MessageClientV2();
        m.start();
    }
}