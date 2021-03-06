import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


// Item Listener for CheckB class

public class Listener implements ItemListener {

    private CheckB ch;

      public static void main(String[] args) {
        CheckB check = new CheckB();
        check.start();
    }

    void setUp() {
        this.ch = new CheckB(); 
    }

    public void itemStateChanged(ItemEvent ev) {
        String state = "Off";

        if (ch.verifyCheckBox())
            state = "On";

        try {
                System.out.println("Check Box is " + state);

            } catch (Exception ex) {
                System.out.println("Oops! Bad thing\n" + ex);
            }
    }
}