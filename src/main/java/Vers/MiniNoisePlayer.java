
// Version Two

import javax.sound.midi.*;

public class MiniNoisePlayer implements ControllerEventListener {
    
    public static void main(String[] args) {
            MiniNoisePlayer player = new MiniNoisePlayer();
            player.go();
    }

    public void go() {  
        try {
                
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();

                int[] eventsControlled = {127};
                sequencer.addControllerEventListener(this, eventsControlled);
                Sequence sequence = new Sequence(Sequence.PPQ, 4);
                Track track = sequence.createTrack();

                for (int i = 5; i < 60; i+=4) {
                    track.add(createEvent(144, 1, i, 100, i));

                    // Controller Event 176
                    track.add(createEvent(176, 1, 127, 0, i));
                    track.add(createEvent(144, 1, i, 100, i + 2));

            }

            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(220);
            sequencer.start();
        
        } catch (Exception ex) {
            System.out.println("Oops! Falied to create PLayer\n" + ex);
        }
    }
    
    public void controlChange(ShortMessage event) {
                System.out.println("la");
            }


    public static MidiEvent createEvent(int pow, int chan, int note, int strength, int time) {
        MidiEvent event =null;

        try {
            ShortMessage msgOne = new ShortMessage();

            msgOne.setMessage(pow, chan, note, strength);
            event = new MidiEvent(msgOne, time);
        
        } catch (Exception ex) {
            System.out.println("Something bad happened" + ex);
        }

        return event;
    }
}
