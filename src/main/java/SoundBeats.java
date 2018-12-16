import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import javax.sound.midi.*;

import java.util.ArrayList;

public class SoundBeats {
   
    JFrame frame;
    JPanel beatPanel;
    Sequencer sequncer;
    Sequence sequnce;
    Track track;
    ArrayList<JCheckBox> checkBoxes;

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
        
    }

}