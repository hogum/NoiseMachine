import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


import javax.sound.midi.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.GridLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
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
    JTextField incomingText;
    JList textList;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    List<JCheckBox> checkBoxesList;
    int textCount;
    Vector<String> displayList = new Vector<String>();
    String userName;
    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;
    HashMap<String, boolean []> inputMap = new HashMap<String, boolean []>();

    String [] soundNames = {
        "Bass Drum", "Closed Hi-Hat",
        "Open Hi-Hat","Acoustic Snare", "Crash Cymbal", "Hand Clap",
        "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga",
        "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo",
        "Open Hi Conga"
    };

    int [] soundNotes = {35, 42, 46, 38, 49, 39,
                       50, 60, 70, 72, 64, 56, 58, 47, 67, 63};


    public void startPlay(String name, String addr, int port) {

        userName = name;

        createConnection(addr, port);
        buildGui();
    }

    public class ClientReader implements Runnable {

        Thread thread;
        boolean[] checkBoxesStatus;
        String receivedString, threadName;
        Object incomingData;

        ClientReader(String name) {
            this.threadName = name;
        }

        public void run() {
            System.out.print("Fetching messages ");

            try {
                    System.out.println("Done!");
                    while((incomingData = inputStream.readObject()) != null) {
                        System.out.println("Incoming... That looks like data ");
                        System.out.println("Data class    " + incomingData.getClass());
                        receivedString = (String) incomingData;
                        checkBoxesStatus = (boolean []) inputStream.readObject();
                        inputMap.put(receivedString, checkBoxesStatus);
                        displayList.add(receivedString);
                        textList.setListData(displayList);
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public void start() {

            if (thread == null) {
                thread = new Thread(this, threadName);
                System.out.println("Created thread  " + threadName);
            }

            System.out.println("Calling " + threadName);
            thread.start();
        }
    }

    private void createConnection(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            ClientReader client = new ClientReader("Client Thread");
            client.start();
        
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void buildGui() {
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

        JButton sendButton = new JButton("Send Track");
        sendButton.addActionListener(new SendButtonListener());
        buttonBox.add(sendButton);
        
        incomingText = new JTextField();
        buttonBox.add(incomingText);
        
        textList = new JList();
        textList.addListSelectionListener(new TextListSelectionListener());
        textList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane txtListPane = new JScrollPane(textList);
        buttonBox.add(txtListPane);
        textList.setListData(displayList);


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

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Beats File");
        JMenuItem restoreBeatMenuItem = new JMenuItem("Restore Beat");
        JMenuItem saveBeatMenuItem = new JMenuItem("Save");

        saveBeatMenuItem.addActionListener(new SerializeButtonListener());
        restoreBeatMenuItem.addActionListener(new RestoreButtonListener());

        fileMenu.add(saveBeatMenuItem);
        fileMenu.add(restoreBeatMenuItem);
        menuBar.add(fileMenu);

        frame.setJMenuBar(menuBar);
        frame.setBounds(350, 90, 300, 300);
        frame.pack();
        frame.setVisible(true);

        setUpMidi();
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


    private void startTracks() {
    
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

    private void buildTracks(int[] trackList) {

        for (int i=0; i<=15; i++) {
            int key = trackList[i];

            if (key != 0) {
                track.add(createEvent(144, 9, key, 100, i));
                track.add(createEvent(128, 9, key, 100, i+1));
        }
    }
}

    private MidiEvent createEvent(int command, int channel, int key, int stroke, int time) {

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

    private void updateCheckBoxes(boolean[] checkBoxesStatus) {

        for(int i=0; i< 256; i++) {
            JCheckBox checkB = (JCheckBox) checkBoxesList.get(i);
            
            if(checkBoxesStatus[i]) {
                checkB.setSelected(true);
            
            } else {
                checkB.setSelected(false);
            }
        }
    }

    class SendButtonListener implements ActionListener {
        
        public void actionPerformed(ActionEvent ev) {
            boolean [] checkBoxStatus = new boolean[256];

             for(int i=0; i< 256; i++) {
                JCheckBox checkB = (JCheckBox) checkBoxesList.get(i);
            
                if(checkBoxStatus[i]) {
                    checkB.setSelected(true);
            }
        }
            try {
                outputStream.writeObject(userName + textCount++ + ":  " + incomingText.getText());
                incomingText.setText("");
                outputStream.writeObject(checkBoxStatus);
            
            } catch (Exception ex) {
                
                System.out.println("Oops! Your message just wont go\n");
                ex.printStackTrace();
            }
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

    class TextListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent le) {

            if(! le.getValueIsAdjusting()) {
                String selectedString = (String) textList.getSelectedValue();

                if( selectedString != null) {
                    boolean [] selectedBoxesStates = (boolean []) inputMap.get(selectedString);
                    updateCheckBoxes(selectedBoxesStates);
                    sequencer.stop();
                    startTracks();
                }
            }
        }

    }

    public static void main(String[] args) {
        
        SoundBeatsV2 sb = new SoundBeatsV2();
        sb.startPlay("Test", "127.0.0.1", 5080);
    }
}
