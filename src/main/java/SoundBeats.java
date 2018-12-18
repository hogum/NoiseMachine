import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

import javax.sound.midi.*;

import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SoundBeats {
   
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

    public static void main(String[] args) {
        
        SoundBeats sb = new SoundBeats();
        sb.buildGui();
    }


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

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void buildTracks(int[] trackList) {

        for (int i=0; i<=15; i++) {
            int key = trackList[i];

            if (key != 0) {
                track.add(createEvent(144, 9, key, 100, i));
                track.add(createEvent(128, 9, key, 100, i+1));
            }
        }
    }

    public MidiEvent createEvent(int command, int channel, int key, int stroke, int time) {

        MidiEvent event = null;

        try {
            ShortMessage midiMsg = new ShortMessage();
            midiMsg.setMessage(
                command,
                channel,
                key,
                stroke);
            event = new MidiEvent(midiMsg, time);
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return event;
    }


    public void startTracks() {
    
    /*
    Creates a 16-element array to hold values for each track.
    Value of an intrument that's to play is set as the 'note',
    if not, the value is zero.
    */

        int [] trackList = null;

        // Clear old track

        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i=0; i<=15; i++) {
            
            trackList = new int[16];

            int note = soundNotes[i];

            for (int j=0; j<=15; j++) {

                JCheckBox checkB = (JCheckBox) checkBoxesList.get(j + (16*i));

                if (checkB.isSelected()) {
                    trackList[j] = note;
                
                } else {

                    trackList[j] = 0;
                }
            }

            buildTracks(trackList);
            track.add(createEvent(176, 1, 127, 0, 16));
        }

        track.add(createEvent(192, 9, 1, 0, 15));

        try {

                sequencer.setSequence(sequence);
                sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
                sequencer.start();
                sequencer.setTempoInBPM(120);
        
        } catch (Exception ex) {

            ex.printStackTrace();
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
        
            boolean [] checkBoxStatus = new boolean[256];

            for (int i=0; i < 256; i++) {
                JCheckBox checkB = (JCheckBox) checkBoxesList.get(i);

                if (checkB.isSelected()) {

                    checkBoxStatus[i] = true;
                }
            }

            try {

                FileOutputStream fs = new FileOutputStream(new File("Checkbox.ser"));
                ObjectOutputStream os = new ObjectOutputStream(fs);
                os.writeObject(checkBoxStatus);
            
            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }        
    }

}