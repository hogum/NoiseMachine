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

class StopButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            
            sequencer.stop();
        }
    }

    class StartButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            startTracks();
        }

    }

     class TempoUpButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * 1.03));
        }
    }

     class TempoDownButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
           
            float tempoFactor = sequencer.getTempoFactor();
            sequencer.setTempoFactor((float) (tempoFactor * .97));
        }
    }

    
    class SerializeButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
        
            JFileChooser fileSaver = new JFileChooser();
            fileSaver.showSaveDialog(frame);
            File fl =  fileSaver.getSelectedFile();

            boolean [] checkBoxStatus = new boolean[256];

            for (int i=0; i < 256; i++) {
                JCheckBox checkB = (JCheckBox) checkBoxesList.get(i);

                if (checkB.isSelected()) {

                    checkBoxStatus[i] = true;
                }
            }

            try {

                FileOutputStream fs = new FileOutputStream(fl);
                ObjectOutputStream os = new ObjectOutputStream(fs);
                os.writeObject(checkBoxStatus);
            
            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }      
    }

    class RestoreButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {

            boolean [] checkBoxStatus = null;

            JFileChooser fileLoader = new JFileChooser();
            fileLoader.showOpenDialog(frame);

            try {
                    FileInputStream fs = new FileInputStream(fileLoader.getSelectedFile());
                    ObjectInputStream is = new ObjectInputStream(fs);
                    checkBoxStatus = (boolean []) is.readObject();
            
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            for (int i=0; i < 256; i++) {

                JCheckBox checkB = (JCheckBox) checkBoxesList.get(i);

                if (checkBoxStatus[i]) {
                    checkB.setSelected(true);
                
                } else {

                    checkB.setSelected(false);
                }
            }

            sequencer.stop();
            startTracks();
        }
    }
