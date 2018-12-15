import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JFrame;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.*;

import java.awt.event.*;
import java.awt.event.*;


class TextList implements ListSelectionListener {
    String [] entries = {
        "delta",
        "epsilon",
        "zeta",
        "eta"
    };

    JList lst = new JList(entries);

    public static void main(String[] args) {
        TextList txtL = new TextList();
        txtL.start();
    }

    public void start() {

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(lst);
        

        scrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane);

        // Set number of Visible Lines
        lst.setVisibleRowCount(4);

        // Restrict user to single selection
        lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Register for selection event
        lst.addListSelectionListener(this);
    }

    public void valueChanged(ListSelectionEvent lse) {
        
        if (! lse.getValueIsAdjusting()) {
            
            String choice = (String) lst.getSelectedValue();

            System.out.println(choice);
        }
    }
}
