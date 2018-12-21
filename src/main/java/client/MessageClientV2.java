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
            ScrollPaneConstants.VERTICAL_SCROLLBAR_POLICY_ALWAYS);

        setConnection();

        sendButton.addActionListener(new SendButtonListener());

        panel.add(outgoingMessage);
        panel.add(sendButton);
        panel.add(incScroller);

        Thread readerThread = new

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setBounds(350, 150, 400, 200);
        frame.setVisible(true);
    }

}