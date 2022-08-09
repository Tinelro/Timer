import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main {

    public static void main(String[] args) throws NumberFormatException,
            InterruptedException, InvocationTargetException {

        Timers f = new Timers();
        f.setSize(600, 400);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);

        Runnable myGUI = () -> {
            JFrame f1 = new JFrame("ColorsFrame");
            f1.setDefaultCloseOperation(f1.EXIT_ON_CLOSE);
            f1.setSize(400, 400);
            f1.setLocation(20, 20);
        };

        SwingUtilities.invokeAndWait(myGUI);

        int yourSide = JOptionPane.showOptionDialog(null, "Choose option",
                "Option dialog", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new String[]{"Settings", "Close"}, null);

        switch (yourSide) {
            case JOptionPane.NO_OPTION, JOptionPane.CLOSED_OPTION -> System.exit(0);
            case JOptionPane.YES_OPTION -> f.setVisible(true);
        }
    }
}
