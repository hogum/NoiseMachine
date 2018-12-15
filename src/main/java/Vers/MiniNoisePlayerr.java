// Version 3

import javax.sound.midi.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;


public class MiniNoisePlayerr {
    static JFrame frame = new JFrame("A Beats Video");
    static MyDrawPanel panelA;

    public static void main (String[] args) {
        MiniNoisePlayerr playerr = new MiniNoisePlayerr();
        playerr.go();
    }

    public void setUpGui() {
        panelA = new MyDrawPanel();
        frame.setContentPane(panelA);
        frame.setBounds(30, 30, 300, 300);
        frame.setVisible(true);
    }

    public void go() {
        setUpGui();

        try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.addControllerEventListener(panelA, new int[] {127});
                Sequence sequence = new Sequence(Sequence.PPQ, 4);
                Track track =  sequence.createTrack();

                for (int i=0; i < 60; i+=4) {
                    int note = (int) (Math.random() * 50) + 1;

                    track.add(createEvent(144, 1, note, 100, i));
                    track.add(createEvent(176, 1, 127, 0, i));
                    track.add(createEvent(128, 1, note, 100, i+2));
                }

                sequencer.setSequence(sequence);
                sequencer.start();
                sequencer.setTempoInBPM(120);
        } catch (Exception ex) {
            System.out.println("Oops! Something bad happened" + ex);
        }
    }

    public MidiEvent createEvent(int command, int channel, int note, int strgth, int time) {
        MidiEvent event = null;

        try {
            ShortMessage msgOne = new ShortMessage();
            msgOne.setMessage(command, channel, note, strgth);
            event = new MidiEvent(msgOne, time);

        } catch (Exception ex) {
            System.out.println("Oops! Bad!" + ex);
        }

        return event;
    }


    class MyDrawPanel extends JPanel implements ControllerEventListener {

        boolean flag = false;

        public void controlChange(ShortMessage event) {
            flag = true;
            repaint();
        }    


        public void paintComponent(Graphics g) {
            if (flag) {
                Graphics2D g2d = (Graphics2D) g;

                int red = (int) (Math.random() * 250);
                int green = (int) (Math.random() * 250);
                int blue = (int) (Math.random() * 250);

                g.setColor(new Color(red, green, blue));

                int height = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);

                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);

                g.fillRect(x, y, height, width);

                flag = false;
            }
        }
    } 
 }   
