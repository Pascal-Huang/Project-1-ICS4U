import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener{
    private AppMonitor monitor;
    private DataManager dataManager;
    private Pet pet;

    private JPanel petJPanel;
    private JLabel healthLabel, xpLabel, levelLabel; 
    private JButton testButton;

    private boolean isPlaying = true;
    private boolean focused = true;
    private boolean deepWorkMode = false;

    public GUI(Pet p, AppMonitor m, DataManager d){
        this.pet = p;
        this.monitor = m;
        this.dataManager = d;

        setSize(600,600);
        setTitle("Virtual Pet Game");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        petJPanel = new JPanel();
        petJPanel.setBackground(Color.BLACK);
        add(petJPanel, BorderLayout.CENTER);

        testButton = new JButton("Click");
        testButton.setBackground(Color.BLACK);
        petJPanel.add(testButton);
        testButton.addActionListener(this);

        setVisible(true);

        dataManager.loadPetData();
        monitor.closeBlackList(monitor.getBlackList());
        System.out.println(pet.toString());
        startProgram();
    }

    public void startProgram(){
        Thread gameLoop = new Thread(() -> {

            focused = monitor.isFocused(monitor.getBlackList()); 
            while (isPlaying) {
                focused = monitor.isFocused(monitor.getBlackList());
                if (focused) {
                    pet.changeXp(1000);
                    System.out.println("Focused! +1000 XP"); // Console log for debugging is still okay
                } else {
                    pet.changeHealth(-5);
                    pet.changeXp(-200);
                }
                dataManager.savePetData();
                System.out.println(pet.toString());
                try { Thread.sleep(1000); } catch (InterruptedException e) {} // Let CPU breathe                                                                             
        }
        });
        gameLoop.start();
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == testButton) {
            System.out.println("Test");
        }
    }
}
