import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.time.LocalTime;

public class Timers extends JFrame {

    private JPanel mainPanel;
    private JRadioButton countDown;
    private JRadioButton onTime;
    private JButton startCD;
    private JFormattedTextField localTimeNow;
    private JButton stopButton;
    private JButton chooseCOLORButton;
    private JSpinner speedChoice;
    private JFormattedTextField newLocalTime;
    private JSpinner remainingTime;

    private int count;
    private boolean blink;
    private Color color;
    private String newTime;
    private Timer t;
    private Timer blinkTimer;
    private Timer countDownTimer;
    private Timer clockTimer;

    final JFrame f1 = new JFrame("Blinking Colors Frame");
    final JPanel p = new JPanel();

    private void createUIComponents() throws ParseException {
        localTimeNow = new JFormattedTextField(new MaskFormatter("##:##:##"));
        newLocalTime = new JFormattedTextField(new MaskFormatter("##:##:##"));

        SpinnerNumberModel nm = new SpinnerNumberModel(100, 0, null, 1);
        speedChoice = new JSpinner(nm);

        SpinnerNumberModel nm1 = new SpinnerNumberModel(0, 0, null, 1);
        remainingTime = new JSpinner(nm1);
    }

    public Timers() throws InterruptedException, InvocationTargetException {
        this.setContentPane(mainPanel);
        stopButton.setEnabled(false);

        SwingWorker<Object, String> swingWorker = new SwingWorker<>() {

            @Override
            protected Object doInBackground() {
                t = new Timer(500, e -> {
                    localTimeNow.setText(String.valueOf(LocalTime.now()));

                    try {
                        newTime = newLocalTime.getText();
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }

                    if (onTime.isEnabled()) {
                        clockTimer = new Timer(1000, e1 -> {
                            if (newTime.equals(localTimeNow.getText())) {
                                blinkTimer.start();
                            }
                        });
                    }
                });

                t.setRepeats(true);
                t.setCoalesce(true);
                t.setInitialDelay(0);
                t.start();

                return null;
            }

            @Override
            protected void done() {
                System.out.println("Clock status: RUNNING ");
            }
        };
        swingWorker.execute();

        f1.setSize(400, 400);
        f1.setLocation(1, 1);
        p.setSize(400, 400);
        f1.add(p);

        chooseCOLORButton.addActionListener(e -> {
            color = JColorChooser.showDialog(null, "chooseColor", Color.WHITE);
            p.setBackground(color);
            chooseCOLORButton.setBackground(color);
        });

        Runnable myGUI = () -> {
            blinkTimer = new Timer(0, e -> {
                blinkTimer.setDelay((int) speedChoice.getValue());
                blink = !blink;
                p.setBackground(blink ? color : Color.WHITE);
                p.setOpaque(blink);
                repaint();
                f1.setVisible(true);
            });
            blinkTimer.setRepeats(true);
            blinkTimer.setCoalesce(true);
        };

        SwingUtilities.invokeAndWait(myGUI);

        countDownTimer = new Timer(1000, e -> {
            if (count > 0 & remainingTime.getValue() != null) {
                remainingTime.setValue(count--);
                blinkTimer.stop();

            } else if (count == 0) {
                countDownTimer.stop();
                blinkTimer.start();
                remainingTime.setValue(0);
                f1.setVisible(true);

            } else {
                remainingTime.setValue(0);
            }
        });

        startCD.addActionListener(e -> {
            try {
                count = (int) remainingTime.getValue();
            } catch (Exception exc) {
                remainingTime.setValue("Insert a number!");
            }

            if (remainingTime.isEnabled()) {
                countDownTimer.start();
            } else {
                clockTimer.start();
            }

            t.start();
            blinkTimer.stop();
            stopButton.setEnabled(true);
            onTime.setEnabled(false);
            countDown.setEnabled(false);
            chooseCOLORButton.setEnabled(false);
            f1.dispose();
        });

        stopButton.addActionListener(e -> {
            t.stop();
            f1.dispose();
            countDown.setSelected(true);
            countDownTimer.stop();
            clockTimer.stop();
            blinkTimer.stop();
            countDown.setEnabled(true);
            onTime.setEnabled(true);
            localTimeNow.setValue(null);
            localTimeNow.setEnabled(false);
            newLocalTime.setValue(null);
            newLocalTime.setEnabled(false);
            remainingTime.setEnabled(true);
            chooseCOLORButton.setEnabled(true);
            chooseCOLORButton.updateUI();
        });

        onTime.addActionListener(e -> {
            t.start();
            localTimeNow.setEnabled(true);
            remainingTime.setEnabled(false);
            remainingTime.setForeground(Color.BLACK);
            newLocalTime.setValue(LocalTime.now());
            newLocalTime.setForeground(color);
            newLocalTime.setEnabled(true);
            newLocalTime.setEditable(true);
        });

        countDown.addActionListener(e -> {
            newLocalTime.setForeground(Color.BLACK);
            newLocalTime.setEnabled(false);
            newLocalTime.setValue(null);
            newLocalTime.setEditable(false);
            remainingTime.setEnabled(true);
            remainingTime.setForeground(color);
            localTimeNow.setValue(null);
            localTimeNow.setEnabled(false);
            t.stop();
        });
    }
}





