
// Version One

import javax.sound.midi.*;

class MiniNoisePlayer {
    
    public static void main(String[] args) {

        try {
                Sequencer sequencer = MidiSystem.getSequencer();
                sequencer.open();

                Sequence sequence = new Sequence(Sequence.PPQ, 4);
                Track track = sequence.createTrack();

                for (int i = 5; i < 60; i+=4) {
                    track.add(createEvent(144, 1, i, 100, i));
                    track.add(createEvent(144, 1, i, 100, 1));

            }

            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(220);
            sequencer.start();
        
        } catch (Exception ex) {
            System.out.println("Oops! Falied to create PLayer\n" + ex);
        }
        
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