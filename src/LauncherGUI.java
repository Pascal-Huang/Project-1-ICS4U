import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LauncherGUI extends JFrame {
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private final Color BG_COLOR = new Color(30, 100, 200); // Your nice blue
    
    public LauncherGUI() {
        setTitle("StuddyBuddy");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- SCREEN 1: WELCOME ---
        JPanel welcomePanel = createWelcomePanel();
        mainPanel.add(welcomePanel, "WELCOME");

        // --- SCREEN 2: PROFILES ---
        JPanel profilePanel = createProfilePanel();
        mainPanel.add(profilePanel, "PROFILES");

        add(mainPanel);
        setVisible(true);
    }

    // ------------------------------------
    // SCREEN 1: WELCOME (The Cat + Title)
    // ------------------------------------
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();

        // 1. Title
        JLabel title = new JLabel("Welcome to StuddyBuddy");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        
        c.gridx = 0; c.gridy = 0;
        c.insets = new Insets(0, 0, 30, 0);
        panel.add(title, c);

        // 2. Cat Image (Reuse your asset)
        ImageIcon catIcon = loadIcon("/assets/cat icon/idle_000.png", 200, 200);
        JLabel catLabel = new JLabel(catIcon);
        if (catIcon == null) catLabel.setText("[Cat Image]");
        
        c.gridy = 1;
        c.insets = new Insets(0, 0, 40, 0);
        panel.add(catLabel, c);

        // 3. Start Button (I added this so you can get to profiles!)
        JButton startBtn = createPillButton("Start", new Color(100, 200, 100)); // Greenish
        startBtn.addActionListener(e -> cardLayout.show(mainPanel, "PROFILES"));
        
        c.gridy = 2;
        c.insets = new Insets(0, 0, 10, 0);
        panel.add(startBtn, c);

        // 4. Instructions Button (Purple)
        JButton instructBtn = createPillButton("Instructions", new Color(128, 0, 128)); // Purple
        instructBtn.addActionListener(e -> showInstructions());
        
        c.gridy = 3;
        panel.add(instructBtn, c);

        return panel;
    }

    // ------------------------------------
    // SCREEN 2: PROFILES (The Two Squares)
    // ------------------------------------
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        GridBagConstraints c = new GridBagConstraints();

        // Profile 1 Box
        JButton p1 = createSquareButton("Profile 1");
        p1.addActionListener(e -> launchGame("profile1.txt"));

        c.gridx = 0; c.insets = new Insets(0, 20, 0, 20);
        panel.add(p1, c);

        // Profile 2 Box
        JButton p2 = createSquareButton("Profile 2");
        p2.addActionListener(e -> launchGame("profile2.txt"));

        c.gridx = 1;
        panel.add(p2, c);

        return panel;
    }

    // --- LOGIC TO START THE GAME ---
    private void launchGame(String fileName) {
        // 1. Close this launcher window
        this.dispose(); 

        // 2. Initialize the Game Objects
        Pet pet = new Pet("Buddy"); // Default name, will load real name from file
        AppMonitor monitor = new AppMonitor();
        DataManager dataManager = new DataManager(pet, monitor, fileName);

        // 3. Launch Main GUI
        SwingUtilities.invokeLater(() -> {
            new TestGUI(pet, monitor, dataManager);
        });
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(this, 
            "1. Select a profile.\n2. Keep studying to earn XP.\n3. Avoid blocked apps or your pet loses health!",
            "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- HELPERS ---
    private JButton createPillButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        // Simple rounded logic not shown for brevity, using standard button for now
        return btn;
    }

    private JButton createSquareButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 150));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false); // Transparent center
        btn.setFocusPainted(false);
        // White line border
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        
        // Hover effect (Optional)
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3)); }
            public void mouseExited(MouseEvent e) { btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); }
        });
        
        return btn;
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_REPLICATE);
                return new ImageIcon(img);
            }
        } catch (Exception e) {}
        return null;
    }
}