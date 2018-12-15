import java.awt.event.ItemEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;

// Check box

public class CheckB {

    JCheckBox checkBox;

    public void start() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        checkBox = new JCheckBox();
        Listener lst = new Listener();

        checkBox.addItemListener(lst);
    }

    public boolean verifyCheckBox() {

        return checkBox.isSelected();
    }
}

