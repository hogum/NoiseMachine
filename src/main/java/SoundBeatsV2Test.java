import org.junit.*;

public class SoundBeatsV2Test {

    @Test
    public void runningClassCreatesGui_thenFileMenuPositionedOnMenuBar() {

    SoundBeatsV2 sb = new SoundBeatsV2();
    sb.startPlay("Test", "127.0.0.1", 5080);
    }
}