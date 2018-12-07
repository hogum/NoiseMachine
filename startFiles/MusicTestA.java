import javax.sound.midi.*;

public class MusicTestA {

    public void play(){

    try {
            Sequencer sequencer = MidiSystem.getSequencer();
    
    } catch(MidiUnavailableException ex) {
        
        System.out.println("Ooops");
    }
    
    }

    public static void main(String[] args) {
        MusicTestA musicTest = new MusicTestA();
        musicTest.play();
    }
}