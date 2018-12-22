import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import javax.sound.midi.*;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.ServerSocket;


public class SoundBeatsV2 {

    JFrame frame;
    JPanel beatPanel;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    ArrayList<JCheckBox> checkBoxesList;

    String [] soundNames = {
        "Bass Drum", "Closed Hi-Hat",
        "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
        "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
        "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
        "Open Hi Conga"
    };

    int [] soundNotes = {35, 42, 46, 38, 49, 39,
                       50, 60, 70, 72, 64, 56, 58, 47, 67, 63};

    
    public void buildGui() {
        frame = new JFrame("Sound Box");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BorderLayout bLayout = new BorderLayout();

        JPanel background = new JPanel(bLayout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        checkBoxesList = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton startButton =  new JButton("Start");
        startButton.addActionListener(new StartButtonListener());
        buttonBox.add(startButton);

        JButton stopButton =  new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());
        buttonBox.add(stopButton);

        JButton tempoUpButton =  new JButton("Tempo Up");
        tempoUpButton.addActionListener(new TempoUpButtonListener());
        buttonBox.add(tempoUpButton);

        JButton tempoDownButton =  new JButton("Tempo Down");
        tempoDownButton.addActionListener(new TempoDownButtonListener());
        buttonBox.add(tempoDownButton);

        JButton serializeButton = new JButton("Send to File");
        serializeButton.addActionListener(new SerializeButtonListener());
        buttonBox.add(serializeButton);

        JButton restoreButton = new JButton("Restore");
        restoreButton.addActionListener(new RestoreButtonListener());
        buttonBox.add(restoreButton);

        Box nameBox = new Box(BoxLayout.Y_AXIS);

        for (int i=0; i < 16 ; i++)
            nameBox.add(new Label(soundNames[i]));
        
        background.add(BorderLayout.WEST, buttonBox);
        background.add(BorderLayout.EAST, nameBox);

        frame.getContentPane().add(background);

        GridLayout gridLayout = new GridLayout(16, 16);
        //gridLayout.setVgap(1);
        gridLayout.setHgap(2);
        
        beatPanel = new JPanel(gridLayout);
        background.add(BorderLayout.CENTER, beatPanel);

        for (int i=0; i<= 255; i++) {
            JCheckBox checkB = new JCheckBox();
            checkB.setSelected(false);
            checkBoxesList.add(checkB);
            beatPanel.add(checkB);
        }

        setUpMidi();

        frame.setBounds(350, 90, 300, 300);
        frame.pack();
        frame.setVisible(true);

    }

}

