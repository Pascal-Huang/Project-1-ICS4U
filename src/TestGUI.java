import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TestGUI extends JFrame {
    // Logic Objects
    private Pet pet;
    private AppMonitor monitor;
    private DataManager dataManager;

    // Components
    private JLabel petImageLabel, nameLabel, statusLabel;
    private JProgressBar healthBar;
    private JProgressBar xpBar;
    private JToggleButton deepWorkToggle;
    private JButton pauseButton, settingsButton;

    // State Variables
    private boolean isPlaying = false;
    private boolean focused = true;
    private boolean deepWorkMode = false;

    // Titlescreen
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    // Colors
    private final Color BG_COLOR = new Color(90, 122, 232); // The nice Blue
    private final Color TEXT_COLOR = Color.WHITE;

    //Animation arrays
    private ImageIcon[] idleAnimation;
    private ImageIcon[] pauseButtonToggle;
    private ImageIcon[] deepWorkAnimation;
    int frameIndex = 0;

    public TestGUI(Pet p, AppMonitor m, DataManager d) {
        this.pet = p;
        this.monitor = m;
        this.dataManager = d;

        // Frame Setup
        setTitle("StuddyBuddy");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); 
        
        // Main Panel background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);
        add(mainPanel);

        GridBagConstraints c = new GridBagConstraints();

        // TOP LEFT: Pause + Deep Work
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5)); // 2 rows, gap of 10
        leftPanel.setOpaque(false); // Transparent so blue shows through
        
        pauseButton = createButton("/assets/pause btn.png.png", "Pause Game", 70, 70);
        pauseButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        leftPanel.add(pauseButton);

        deepWorkToggle = new JToggleButton("Deep Work: OFF");
        styleButton(deepWorkToggle);
        leftPanel.add(deepWorkToggle);

        // GridBag Placement for Left Panel
        c.gridx = 0; c.gridy = 0; // Top Left
        c.weightx = 0.2; c.weighty = 0.0; // Takes up 20% width
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(10, 10, 0, 0); // Padding
        mainPanel.add(leftPanel, c);


        // TOP CENTER: Health Bar + Name
        JPanel centerTopPanel = new JPanel(new GridBagLayout()); 
        centerTopPanel.setOpaque(false);
        GridBagConstraints topC = new GridBagConstraints();

        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(pet.getHealth());
        healthBar.setForeground(new Color(255, 80, 80)); // Red/Pink
        healthBar.setBackground(new Color(100, 30, 30)); // Dark red background
        healthBar.setPreferredSize(new Dimension(200, 15)); // Make it thick like pixel art
        centerTopPanel.add(healthBar);

        topC.gridx = 0; topC.gridy = 0;
        topC.insets = new Insets(0, 0, 5, 0); // Gap below bar
        centerTopPanel.add(healthBar, topC);

        nameLabel = new JLabel("name", SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setForeground(TEXT_COLOR);
                
        topC.gridx = 0; topC.gridy = 1;
        centerTopPanel.add(nameLabel, topC);


        // GridBag Placement for Top Center
        c.gridx = 1; c.gridy = 0;
        c.weightx = 0.6; 
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.NORTH;
        c.insets = new Insets(15, 0, 0, 0);
        mainPanel.add(centerTopPanel, c);


        // TOP RIGHT: Settings button
        settingsButton = createButton("/assets/settings btn.png", "Settings", 50, 50);
        settingsButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        
        // GridBag Placement for Top Right
        c.gridx = 2; c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.0; 
        c.anchor = GridBagConstraints.NORTHEAST;
        c.insets = new Insets(10, 0, 0, 10);
        mainPanel.add(settingsButton, c);


        // CENTER: Pet Image Icon
        petImageLabel = new JLabel(loadIcon("/assets/cat icon/idle_000.png", WIDTH, HEIGHT));
        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        


        // GridBag Placement for Center
        c.gridx = 0; c.gridy = 1;

        c.gridwidth = 3; 
        c.weightx = 1.0; 
        c.weighty = 1.0;


        c.anchor = GridBagConstraints.CENTER; 
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 80, 80, 0); //Pet placement adjustemnt 
        mainPanel.add(petImageLabel, c);


        // BOTTOM: XP Bar + Status
        
        // XP Bar
        xpBar = new JProgressBar(0, 2000); // 0 to 2000 XP
        xpBar.setValue((int)pet.getRequiredXp(pet.getLevel() + 1));
        xpBar.setForeground(Color.CYAN);
        xpBar.setBackground(new Color(0, 50, 100));
        xpBar.setStringPainted(true); // Shows "1000/2000"
        xpBar.setPreferredSize(new Dimension(300, 20));

        c.gridx = 0; c.gridy = 2;
        c.gridwidth = 3;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.SOUTH; // Sit just below the pet
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(xpBar, c);

        // Status Button (Bottom Right)
        statusLabel = new JLabel("Status: Scanning...", SwingConstants.CENTER);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(255, 255, 255, 50)); // Transparent white
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        statusLabel.setPreferredSize(new Dimension(150, 30));

        c.gridx = 2; c.gridy = 3; // Bottom Right corner
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.insets = new Insets(0, 0, 10, 10);
        mainPanel.add(statusLabel, c);


        // 3. Add Listeners
        setupListeners();

        // 4. Start the Game Loop
        dataManager.loadData();
        monitor.closeBlackList(monitor.getBlackList());
        System.out.println(pet.toString());
        loadAnimations();
        startAnimation(idleAnimation, petImageLabel);

        startProgram();
        
        setVisible(true);
    }
    // private JPanel buildGamePanel(){
    //     JPanel gamePanel = new JPanel(new GridBagLayout());
    //     GridBagConstraints c = new GridBagConstraints();

    //     // TOP LEFT: Pause + Deep Work
    //     JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5)); // 2 rows, gap of 10
    //     leftPanel.setOpaque(false); // Transparent so blue shows through
        
    //     pauseButton = createButton("/assets/pause btn.png.png", "Pause Game", 70, 70);
    //     pauseButton.setFont(new Font("SansSerif", Font.BOLD, 20));
    //     leftPanel.add(pauseButton);

    //     deepWorkToggle = new JToggleButton("Deep Work: OFF");
    //     styleButton(deepWorkToggle);
    //     leftPanel.add(deepWorkToggle);

    //     // GridBag Placement for Left Panel
    //     c.gridx = 0; c.gridy = 0; // Top Left
    //     c.weightx = 0.2; c.weighty = 0.0; // Takes up 20% width
    //     c.anchor = GridBagConstraints.NORTHWEST;
    //     c.insets = new Insets(10, 10, 0, 0); // Padding
    //     gamePanel.add(leftPanel, c);


    //     // TOP CENTER: Health Bar + Name
    //     JPanel centerTopPanel = new JPanel(new GridBagLayout()); 
    //     centerTopPanel.setOpaque(false);
    //     GridBagConstraints topC = new GridBagConstraints();

    //     healthBar = new JProgressBar(0, 100);
    //     healthBar.setValue(pet.getHealth());
    //     healthBar.setForeground(new Color(255, 80, 80)); // Red/Pink
    //     healthBar.setBackground(new Color(100, 30, 30)); // Dark red background
    //     healthBar.setPreferredSize(new Dimension(200, 15)); // Make it thick like pixel art
    //     centerTopPanel.add(healthBar);

    //     topC.gridx = 0; topC.gridy = 0;
    //     topC.insets = new Insets(0, 0, 5, 0); // Gap below bar
    //     centerTopPanel.add(healthBar, topC);

    //     nameLabel = new JLabel("name", SwingConstants.CENTER);
    //     nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
    //     nameLabel.setForeground(TEXT_COLOR);
                
    //     topC.gridx = 0; topC.gridy = 1;
    //     centerTopPanel.add(nameLabel, topC);


    //     // GridBag Placement for Top Center
    //     c.gridx = 1; c.gridy = 0;
    //     c.weightx = 0.6; 
    //     c.weighty = 0.0;
    //     c.anchor = GridBagConstraints.NORTH;
    //     c.insets = new Insets(15, 0, 0, 0);
    //     gamePanel.add(centerTopPanel, c);


    //     // TOP RIGHT: Settings button
    //     settingsButton = createButton("/assets/settings btn.png", "Settings", 50, 50);
    //     settingsButton.setFont(new Font("SansSerif", Font.BOLD, 25));
        
    //     // GridBag Placement for Top Right
    //     c.gridx = 2; c.gridy = 0;
    //     c.weightx = 0.2;
    //     c.weighty = 0.0; 
    //     c.anchor = GridBagConstraints.NORTHEAST;
    //     c.insets = new Insets(10, 0, 0, 10);
    //     gamePanel.add(settingsButton, c);


    //     // CENTER: Pet Image Icon
    //     petImageLabel = new JLabel(loadIcon("/assets/cat icon/idle_000.png", WIDTH, HEIGHT));
    //     petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        


    //     // GridBag Placement for Center
    //     c.gridx = 0; c.gridy = 1;

    //     c.gridwidth = 3; 
    //     c.weightx = 1.0; 
    //     c.weighty = 1.0;


    //     c.anchor = GridBagConstraints.CENTER; 
    //     c.fill = GridBagConstraints.NONE;
    //     c.insets = new Insets(0, 80, 80, 0); //Pet placement adjustemnt 
    //     gamePanel.add(petImageLabel, c);


    //     // BOTTOM: XP Bar + Status
        
    //     // XP Bar
    //     xpBar = new JProgressBar(0, 2000); // 0 to 2000 XP
    //     xpBar.setValue((int)pet.getRequiredXp(pet.getLevel() + 1));
    //     xpBar.setForeground(Color.CYAN);
    //     xpBar.setBackground(new Color(0, 50, 100));
    //     xpBar.setStringPainted(true); // Shows "1000/2000"
    //     xpBar.setPreferredSize(new Dimension(300, 20));

    //     c.gridx = 0; c.gridy = 2;
    //     c.gridwidth = 3;
    //     c.weighty = 0.0;
    //     c.anchor = GridBagConstraints.SOUTH; // Sit just below the pet
    //     c.insets = new Insets(0, 0, 10, 0);
    //     gamePanel.add(xpBar, c);

    //     // Status Button (Bottom Right)
    //     statusLabel = new JLabel("Status: Scanning...", SwingConstants.CENTER);
    //     statusLabel.setOpaque(true);
    //     statusLabel.setBackground(new Color(255, 255, 255, 50)); // Transparent white
    //     statusLabel.setForeground(Color.WHITE);
    //     statusLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    //     statusLabel.setPreferredSize(new Dimension(150, 30));

    //     c.gridx = 2; c.gridy = 3; // Bottom Right corner
    //     c.gridwidth = 1;
    //     c.anchor = GridBagConstraints.SOUTHEAST;
    //     c.insets = new Insets(0, 0, 10, 10);
    //     gamePanel.add(statusLabel, c);
    // }

    // --- Helper to make buttons look nice ---
    private ImageIcon loadIcon(String fileName, int width, int height) {
        try{
            // Get image URL from resources
            java.net.URL imgURL = getClass().getResource(fileName);
            if(imgURL != null){
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            else{
                System.out.println("Couldn't find file: " + fileName);
                return null;
            }
        }
        catch(Exception e){
            System.out.println("Error loading image: " + e.getMessage());
            return null;
        }
    }
    private void loadAnimations(){
        idleAnimation = new ImageIcon[10];
        pauseButtonToggle = new ImageIcon[2];
        for (int i = 0; i < idleAnimation.length; i++){
            idleAnimation[i] = loadIcon("/assets/cat icon/idle_00" + i + ".png", 220, 220); //Adjust size for image icons
        }
        pauseButtonToggle[0] = loadIcon("/assets/pause btn.png.png", 70, 70);
        pauseButtonToggle[1] = loadIcon("/assets/play btn.png.png", 70, 70);
    }
    private void startAnimation(ImageIcon[] animation, JLabel label){
        
        Timer animationTimer = new Timer(170, e -> {
            if (!isPlaying) return; // Pause animation when game is paused
            frameIndex ++;
            if (frameIndex >= animation.length - 1){
                frameIndex = 0;
            }
            label.setIcon(animation[frameIndex]);
        });
        animationTimer.start();
    }
    private JButton createButton(String fileName, String buttonText, int width, int height) {
        JButton btn = new JButton();
        ImageIcon icon = loadIcon(fileName, width, height); // Size adjustment for buttpns
        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText("?"); // Fallback if image fails
        }
        // Make the button transparent
        btn.setContentAreaFilled(false); // No background color
        btn.setBorderPainted(false);     // No border line
        btn.setFocusPainted(false);      // No blue outline when clicked
        btn.setToolTipText(buttonText);
        
        return btn;
    }

    private void styleButton(AbstractButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(BG_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    
    //Logic & Listeners
    private void setupListeners() {
        pauseButton.addActionListener(e -> {
            isPlaying = !isPlaying;
            statusLabel.setText(isPlaying ? "Game Resumed" : "Game Paused");
            if (isPlaying) {
                pauseButton.setIcon(pauseButtonToggle[0]); // Show pause icon when playing
            } else {
                pauseButton.setIcon(pauseButtonToggle[1]); // Show play icon when paused
            }
        });

        deepWorkToggle.addActionListener(e -> {
            boolean mode = deepWorkToggle.isSelected();
            monitor.setDeepWork(mode);
            // Assuming AppMonitor has a setter
            // monitor.setDeepWork(mode); 
            deepWorkToggle.setText(monitor.getDeepWork() ? "Deep Work: ON" : "Deep Work: OFF");
            deepWorkToggle.setBackground(mode ? Color.RED : Color.WHITE);
            deepWorkToggle.setForeground(mode ? Color.WHITE : BG_COLOR);
        });
        
        settingsButton.addActionListener(e -> {
            SettingsGUI settingsScreen = new SettingsGUI(monitor, dataManager);
            settingsScreen.addWindowListener(new java.awt.event.WindowAdapter(){
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    togglePause();
                }
            });
            if (isPlaying) {
                togglePause(); // Pause the game when settings is opened
            }
            settingsScreen.setVisible(true);
        });
    }

    public void startProgram() {
        Thread gameLoop = new Thread(() -> {
            while(true){
                if (isPlaying){
                    SwingUtilities.invokeLater(() -> statusLabel.setText("Status: Scanning...")); // Update Status to "Scanning"

                    boolean isFocused = monitor.isFocused(monitor.getBlackList()); 
                    if (isFocused) {
                        pet.changeXp(1000);
                    } else {
                        pet.changeHealth(-5);
                        pet.changeXp(-200);
                    }
                    dataManager.saveData();
                    System.out.println(pet.toString());

                    // Update UI on main thread
                    SwingUtilities.invokeLater(() -> {
                        healthBar.setValue(pet.getHealth());
                        xpBar.setValue((int)pet.getRequiredXp(pet.getLevel() + 1));
                        xpBar.setString("XP: " + pet.getXp() + "/" + pet.getRequiredXp(pet.getLevel() + 1));
                        
                        if (isFocused) statusLabel.setText("Status: Focused");
                        else statusLabel.setText("Status: Distracted!");
                    });
                }
                else{
                    try { 
                    Thread.sleep(1000); //Gives CPU a rest
                    } catch (InterruptedException e) { 
                        e.printStackTrace(); 
                    }
                }
            }
        });
        gameLoop.start();
    }
    public void togglePause(){
        isPlaying = !isPlaying;
        if (isPlaying) {
            pauseButton.setIcon(pauseButtonToggle[0]); // Show pause icon when playing
            statusLabel.setText("Game Resumed test");
        } 
        else {
            pauseButton.setIcon(pauseButtonToggle[1]); // Show play icon when paused
            statusLabel.setText("Game Paused");
        }
    }
}