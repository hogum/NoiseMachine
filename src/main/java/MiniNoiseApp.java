import javax.sound.midi.*;

public class MiniNoiseApp {

    public static void main(String[] args) {
        MiniNoiseApp noiseApp = new MiniNoiseApp();
        noiseApp.play();
    }

    public void play() {

        try {
            
            Sequencer player = MidiSystem.getSequencer();
            player.open();

            Sequence sequence = new Sequence(Sequence.PPQ, 4);

            Track track = sequence.createTrack();

            ShortMessage one = new ShortMessage();
            one.setMessage(192, 1, 102, 0);
            MidiEvent noteChange = new MidiEvent(one, 1);
            track.add(noteChange);

            ShortMessage msgOne = new ShortMessage();
            msgOne.setMessage(144, 1, 44, 100);
            MidiEvent noteOn = new MidiEvent(msgOne, 3);
            track.add(noteOn);

            ShortMessage msgTwo = new ShortMessage();
            msgTwo.setMessage(128,1, 44, 100);
            MidiEvent noteOff = new MidiEvent(msgTwo, 16);
            track.add(noteOff);

            player.setSequence(sequence);

            player.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}