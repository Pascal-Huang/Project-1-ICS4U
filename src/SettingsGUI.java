import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SettingsGUI extends JFrame {

    // Logic Reference
    private DataManager dataManager;
    private AppMonitor monitor;

    // UI Components
    private JPanel listPanel; // The container inside the scroll pane
    private JTextField inputField;
    private JButton programsTab, keywordsTab;
    
    // State
    private boolean isProgramTab = true; // true = Programs, false = Keywords
    private final Color BG_COLOR = new Color(90, 122, 232);
    private final Color BTN_ACTIVE = new Color(100, 30, 100); // Darker purple for active
    private final Color BTN_INACTIVE = new Color(150, 50, 150); // Lighter for inactive

    // Local lists to simulate data (Connect these to your AppMonitor later!)
    private ArrayList<String> programList;
    private ArrayList<String> keywordList;

    public SettingsGUI(AppMonitor m, DataManager d) {
        this.monitor = m;
        this.dataManager = d;

        programList = monitor.getBlackList();
        keywordList = monitor.getBlacklistedSites();

        // Load data (Using placeholders for now - connect to monitor.getBlackList() later)

        // 1. Window Setup
        setTitle("Black List Settings");
        setSize(400, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);
        setLocationRelativeTo(null); // Center on screen

        // 2. Header Panel (Title & Tabs)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        // Title
        JLabel titleLabel = new JLabel("Black List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(15)); // Gap

        // Tabs Container
        JPanel tabsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        tabsPanel.setOpaque(false);

        programsTab = createTabButton("Programs", true);
        keywordsTab = createTabButton("Keywords", false);

        tabsPanel.add(programsTab);
        tabsPanel.add(keywordsTab);
        headerPanel.add(tabsPanel);

        add(headerPanel, BorderLayout.NORTH);


        // 3. The List Area (Middle)
        // We use a JPanel inside a ScrollPane to hold our custom rows
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); // Stack items vertically
        listPanel.setBackground(BG_COLOR);
        // Add a simplified border to look like your box
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // The black box outline
        scrollPane.setBackground(BG_COLOR);
        scrollPane.getViewport().setBackground(BG_COLOR);
        // Padding around the scroll box
        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(BG_COLOR);
        scrollContainer.setBorder(new EmptyBorder(10, 40, 10, 40)); // Margins
        scrollContainer.add(scrollPane, BorderLayout.CENTER);

        add(scrollContainer, BorderLayout.CENTER);


        // 4. Input Area (Bottom)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(new EmptyBorder(10, 0, 20, 0));

        inputField = new JTextField(20);
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Placeholder text logic
        TextPrompt placeholder = new TextPrompt("Enter to add (e.g. discord.exe)", inputField);
        placeholder.changeAlpha(0.75f);
        placeholder.changeStyle(Font.ITALIC);

        // Add Listener to Enter Key
        inputField.addActionListener(e -> addItem());

        bottomPanel.add(inputField);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial Load
        refreshList();
        setVisible(true);
    }

    // --- LOGIC METHODS ---

    private void refreshList() {
        listPanel.removeAll(); // Clear current items
        
        ArrayList<String> currentData = isProgramTab ? programList : keywordList;

        for (String item : currentData) {
            listPanel.add(createListItem(item));
            listPanel.add(Box.createVerticalStrut(10)); // Gap between items
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addItem() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) return;

        if (isProgramTab) {
            programList.add(input);
            monitor.addToBlackList(input); 
        } else {
            keywordList.add(input);
            monitor.addToBlackListSites(input);
        }
        
        dataManager.savePetData(); // Save changes
        inputField.setText("");
        refreshList();
    }

    private void deleteItem(String text) {
        if (isProgramTab) {
            programList.remove(text);
        } else {
            keywordList.remove(text);
        }
        dataManager.savePetData();
        refreshList();
    }

    private void switchTab(boolean toPrograms) {
        isProgramTab = toPrograms;
        
        // Update visual colors
        programsTab.setBackground(isProgramTab ? BTN_ACTIVE : BTN_INACTIVE);
        keywordsTab.setBackground(!isProgramTab ? BTN_ACTIVE : BTN_INACTIVE);

        refreshList();
    }

    // --- UI HELPER METHODS ---

    private JPanel createListItem(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(400, 40)); // Fixed height
        panel.setBackground(Color.WHITE); // White box
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Thickness

        // The Text
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(BG_COLOR);
        label.setBorder(new EmptyBorder(0, 10, 0, 0));
        panel.add(label, BorderLayout.CENTER);

        // The Delete Button [X]
        JButton deleteBtn = new JButton("X");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(200, 50, 50)); // Red X
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setPreferredSize(new Dimension(40, 40));
        
        deleteBtn.addActionListener(e -> deleteItem(text));

        panel.add(deleteBtn, BorderLayout.EAST);

        return panel;
    }

    private JButton createTabButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(isActive ? BTN_ACTIVE : BTN_INACTIVE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Rounded look padding
        
        // Make it look like a rounded pill
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        btn.addActionListener(e -> switchTab(text.equals("Programs")));
        return btn;
    }
    
    // Tiny helper class for Placeholder Text (Inner Class)
    class TextPrompt extends JLabel {
        private final JTextField component;
        public TextPrompt(String text, JTextField component) {
            this.component = component;
            setText(text);
            setFont(component.getFont());
            setForeground(Color.GRAY);
            setBorder(new EmptyBorder(0, 5, 0, 0)); // Padding to match input
            component.setLayout(new BorderLayout());
            component.add(this);
            
            // Hide when typing
            component.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent e) {
                    setVisible(component.getText().isEmpty());
                }
            });
        }
        public void changeAlpha(float alpha) { /* Simple stub */ }
        public void changeStyle(int style) { setFont(getFont().deriveFont(style)); }
    }
}