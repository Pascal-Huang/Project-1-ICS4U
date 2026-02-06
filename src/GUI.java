import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GUI extends JFrame{
    private JPanel topJPanel, petJPanel, botttomJPanel;
    private JButton testButton;

    public GUI(){
        setSize(800,600);
        setTitle("Virtual Pet Game");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        petJPanel = new JPanel();
        petJPanel.setBackground(Color.BLACK);
        add(petJPanel, BorderLayout.CENTER);

        testButton = new JButton("Click");
        testButton.setBackground(Color.GRAY);
        petJPanel.add(testButton);


    }
}
