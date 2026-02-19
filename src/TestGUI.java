import javax.swing.*;
import java.awt.*;

public class TestGUI extends JFrame {
    // Main Objects
    private Pet pet;
    private AppMonitor monitor;
    private DataManager dataManager;

    // GUI Components
    private JLabel petImageLabel, leveLabel, statusLabel;
    private JProgressBar healthBar;
    private JProgressBar xpBar;
    private JToggleButton deepWorkToggle;
    private JButton pauseButton, settingsButton, profileButton1, profileButton2;

    // State Variable
    private boolean isPlaying = false;

    // Main screen panel
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    // Colors
    private final Color BG_COLOR = new Color(90, 122, 232); // Custom blue background
    private final Color TEXT_COLOR = Color.WHITE;

    //Animation arrays
    private ImageIcon[] idleAnimation;
    private ImageIcon[] pauseButtonToggle;
    private ImageIcon[] deepWorkAnimation; //Future feature
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

        mainContainer.setBackground(BG_COLOR);
        add(mainContainer);

        JPanel titleScreen = createTitlePanel();
        mainContainer.add(titleScreen, "TITLE");

        cardLayout.show(mainContainer, "TITLE");
        setVisible(true);
    }
    // Creates the title screen panel with profile selection and instructions button
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        GridBagConstraints c = new GridBagConstraints(); //Used to manipulate positioning of components in the grid layout through constraints

        // Title Text
        JLabel title = new JLabel("Welcome to StuddyBuddy");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 2; // Span across center
        c.insets = new Insets(0, 0, 80, 0); // Gap below title
        panel.add(title, c);

        // Profile Buttons Container
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 40, 0)); // 1 row, 2 cols, 40px gap
        buttonPanel.setOpaque(false);

        profileButton1 = new JButton("Profile 1");
        profileButton1.setPreferredSize(new Dimension(110, 120));
        profileButton1.setFont(new Font("SansSerif", Font.BOLD, 20));
        profileButton1.setForeground(TEXT_COLOR);
        profileButton1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        profileButton1.setContentAreaFilled(false);

        // Hover effect for profile buttons
        profileButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                profileButton1.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                profileButton1.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                profileButton1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
                profileButton1.setForeground(TEXT_COLOR);
            }
        });
        profileButton1.addActionListener(e -> initializeGame(1));
        
        profileButton2 = new JButton("Profile 2");
        profileButton2.setPreferredSize(new Dimension(110, 120));
        profileButton2.setFont(new Font("SansSerif", Font.BOLD, 20));
        profileButton2.setForeground(TEXT_COLOR);
        profileButton2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        profileButton2.setContentAreaFilled(false);

        profileButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                profileButton2.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
                profileButton2.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                profileButton2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
                profileButton2.setForeground(TEXT_COLOR );
            }
        });
        profileButton2.addActionListener(e -> initializeGame(2));

        buttonPanel.add(profileButton1);
        buttonPanel.add(profileButton2);

        c.gridy = 1;
        c.insets = new Insets(0, 0, 60, 0); // Gap below buttons
        panel.add(buttonPanel, c);

        // Instructions Button
        JButton instructBtn = createButton("/assets/settings btn.png", "Instructions", 50, 50);
        instructBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "1. Select a profile to start.\n" +
                "2. Press settings button to add/remove programs that will distract you while studying.\n" +
                "3. The app runs in the background while you study.\n" +
                "4. You can turn on deep work mode, which auto shuts down any distracting apps.\n" +  
                "5. Avoid blocked apps to keep your pet happy while leveling up!",
                "How to Play", JOptionPane.INFORMATION_MESSAGE);
        });

        c.gridy = 2;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(instructBtn, c);

        return panel;
    }

    private void initializeGame(int filePath) { // Load data and initialize game screen
        dataManager.setFilePath(filePath);
        dataManager.loadData();
        if (pet.isAlive() == false){ // If pet is dead, reset data to initial values
            dataManager.loadDefaultData();
            System.out.println("Loaded default data");
        }
        // Create Game Panel
        JPanel gamePanel = createGamePanel();
        mainContainer.add(gamePanel, "GAME");
        cardLayout.show(mainContainer, "GAME");

        // Start Loops
        monitor.closeBlackList(monitor.getBlackList()); // Close any blacklisted apps that are currently open when game starts
        isPlaying = true;
        loadAnimations();
        startAnimation(idleAnimation, petImageLabel);
        startGame(); 
    }
    private JPanel createGamePanel() { // Creates the main game GUI with all necessary pet stats info
        JPanel gamePanel = new JPanel(new GridBagLayout());
        gamePanel.setBackground(BG_COLOR);
        GridBagConstraints c = new GridBagConstraints(); //Using constraints to manipulate positioning of components

        // --- TOP HEADER (LEFT) ---
        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        leftPanel.setOpaque(false);
        pauseButton = createButton("/assets/pause btn.png.png", "Pause Game", 70, 70);
        pauseButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        leftPanel.add(pauseButton);

        deepWorkToggle = new JToggleButton("Deep Work: OFF"); //Deep work button
        deepWorkToggle.setFocusPainted(false);
        deepWorkToggle.setBackground(Color.WHITE);
        deepWorkToggle.setForeground(BG_COLOR);
        deepWorkToggle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        deepWorkToggle.setFont(new Font("SansSerif", Font.BOLD, 12));
        leftPanel.add(deepWorkToggle);

        //Control the positioning and spacing of the left panel in the grid
        c.gridx = 0; c.gridy = 0;
        c.weightx = 0.2; c.weighty = 0.0;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(10, 10, 0, 0);
        gamePanel.add(leftPanel, c);

        // --- TOP HEADER (CENTER - HEALTH & LEVEL) ---
        JPanel centerTopPanel = new JPanel(new GridBagLayout());
        centerTopPanel.setOpaque(false);
        GridBagConstraints topC = new GridBagConstraints(); //Specefic constraints on the top center panel

        // Health Bar
        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(pet.getHealth());
        healthBar.setString(pet.getHealth() + "/100 health");
        healthBar.setForeground(new Color(255, 80, 80));
        healthBar.setBackground(new Color(100, 30, 30));
        healthBar.setPreferredSize(new Dimension(200, 25));
        healthBar.setStringPainted(true);

        healthBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
        protected Color getSelectionBackground() { // Allows for text colour to be changed
            return Color.WHITE; // Text color when over the empty (dark red) part
        }
        protected Color getSelectionForeground() {
            return Color.WHITE; // Text color when over the filled (bright red) part
        }
        });
        //Healthbar placement on top center panel
        topC.gridx = 0; topC.gridy = 0;
        topC.fill = GridBagConstraints.HORIZONTAL; 
        topC.weightx = 1.0;
        topC.insets = new Insets(0, 0, 5, 0); //Adjust healthbar positioning
        centerTopPanel.add(healthBar, topC);

        leveLabel = new JLabel("Level: " + pet.getLevel(), SwingConstants.CENTER);
        leveLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        leveLabel.setForeground(TEXT_COLOR);

        //Level label placement on top center panel
        topC.gridy = 1;
        topC.fill = GridBagConstraints.HORIZONTAL;
        topC.weightx = 1.0;
        centerTopPanel.add(leveLabel, topC);

        // Add Center Top Panel to Main Grid
        c.gridx = 1; c.gridy = 0;
        c.weightx = 0.6; 
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL; // Ensure the panel itself stretches
        c.insets = new Insets(130, 100, 0, 0); //Adjust position of health + name labels
        gamePanel.add(centerTopPanel, c);

        // --- TOP HEADER (RIGHT) ---
        //Settings Button
        settingsButton = createButton("/assets/settings btn.png", "Settings", 50, 50);
        settingsButton.setFont(new Font("SansSerif", Font.BOLD, 25));

        c.gridx = 2; c.gridy = 0;
        c.weightx = 0.2; 
        c.anchor = GridBagConstraints.NORTHEAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 0, 0, 10);
        gamePanel.add(settingsButton, c);

        // --- CENTER PET ---
        //Create pet image label and set initial icon
        petImageLabel = new JLabel();
        petImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 3;
        c.weightx = 1.0; c.weighty = 1.0; 
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 80, 70, 0); // Pet positioning
        gamePanel.add(petImageLabel, c);

        // --- BOTTOM STATS (XP) ---
        //xp bar setup
        xpBar = new JProgressBar(0, (int) pet.getRequiredXp(pet.getLevel()+1 ));
        xpBar.setString("XP: " + pet.getXp() + "/" + pet.getRequiredXp(pet.getLevel() + 1));
        xpBar.setValue((int)pet.getXp());
        xpBar.setForeground(Color.CYAN);
        xpBar.setBackground(new Color(0, 50, 100));
        xpBar.setStringPainted(true);
        xpBar.setPreferredSize(new Dimension(300, 20));
        xpBar.setBorderPainted(false); //

        c.gridx = 0; c.gridy = 2;
        c.gridwidth = 3;
        c.weighty = 0.0;
        c.anchor = GridBagConstraints.SOUTH; // Pin to bottom
        c.fill = GridBagConstraints.HORIZONTAL; 
        
        c.insets = new Insets(20, 70, 20, 70); //Xp bar positioning
        gamePanel.add(xpBar, c);

        // --- STATUS LABEL ---
        // Label to show current status (focused, distracted, paused, etc)
        statusLabel = new JLabel("Status: Scanning...", SwingConstants.CENTER);
        statusLabel.setOpaque(true); 
        statusLabel.setBackground(Color.WHITE); 
        statusLabel.setForeground(BG_COLOR);
        statusLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        statusLabel.setPreferredSize(new Dimension(150, 30));

        c.gridx = 2; c.gridy = 3;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 10, 10);
        gamePanel.add(statusLabel, c);
        setupListeners();

        return gamePanel;
    }
    //Icon loader to insert images onto different components
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
    private void loadAnimations(){ // Loads all the frames of the idle animation into an array
        idleAnimation = new ImageIcon[10];
        pauseButtonToggle = new ImageIcon[2];
        for (int i = 0; i < idleAnimation.length; i++){
            idleAnimation[i] = loadIcon("/assets/cat icon/idle_00" + i + ".png", 220, 220); //Adjust size for image icons
        }
        pauseButtonToggle[0] = loadIcon("/assets/pause btn.png.png", 70, 70);
        pauseButtonToggle[1] = loadIcon("/assets/play btn.png.png", 70, 70);
    }
    private void startAnimation(ImageIcon[] animation, JLabel label){ //Starts the loop for animations
        
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
    //Creates button and puts icon images on top
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

    //Logic & Listeners
    private void setupListeners() {
        pauseButton.addActionListener(e -> {
            isPlaying = !isPlaying;
            statusLabel.setText(isPlaying ? "Status: Scanning..." : "Game Paused"); //Swithces between scanning and paused 
            if (isPlaying) { //Toggle pause/play icons
                pauseButton.setIcon(pauseButtonToggle[0]); // Show pause icon when playing
            } else {
                pauseButton.setIcon(pauseButtonToggle[1]); // Show play icon when paused
            }
        });

        deepWorkToggle.addActionListener(e -> {
            boolean mode = deepWorkToggle.isSelected();
            monitor.setDeepWork(mode); //Set deep work mode in monitor
            deepWorkToggle.setText(monitor.getDeepWork() ? "Deep Work: ON" : "Deep Work: OFF");
            deepWorkToggle.setBackground(mode ? Color.RED : Color.WHITE);
            deepWorkToggle.setForeground(mode ? Color.WHITE : BG_COLOR);
        });
        //Creates setting screen where you can add or remove apps from blacklist
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

    public void startGame() { //Main game loop that scans (about 30s), applies rewards / punishements and updates the GUI
        System.out.println(pet.toString());
        Thread gameLoop = new Thread(() -> {
            while(true){
                if (isPlaying){
                    if (!pet.isAlive()){
                        petDied();
                    }
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
                        healthBar.setString(pet.getHealth() + "/100 health");
                        xpBar.setValue((int)pet.getXp());
                        xpBar.setMaximum((int)pet.getRequiredXp(pet.getLevel() + 1));
                        xpBar.setString("XP: " + pet.getXp() + "/" + pet.getRequiredXp(pet.getLevel() + 1));
                        leveLabel.setText("Level: " + pet.getLevel());
                        
                        if (isFocused) {
                            statusLabel.setText("Focused: +1000 XP!");
                            statusLabel.setForeground(new Color(0, 150, 0)); // Dark Green
                        } else {
                            statusLabel.setText("Distracted!!!");
                            statusLabel.setForeground(Color.RED); // Bright Red
                        }
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
            statusLabel.setText("Game Resumed");
        } 
        else {
            pauseButton.setIcon(pauseButtonToggle[1]); // Show play icon when paused
            statusLabel.setText("Game Paused");
        }
    }
    //Opens JOptionPane when pet dies and closes program when they click ok
    public void petDied(){
        isPlaying = false; 
        
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, 
                "Your pet has died due to too many distractions.\n\nGame Over.", 
                "You Died", 
                JOptionPane.ERROR_MESSAGE);
            
            // Close the program when they click OK
            System.exit(0);
        });
        
    }
}