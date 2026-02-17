import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class SettingsGUI extends JFrame {

    // Logic Reference
    private DataManager dataManager;
    private AppMonitor monitor;

    // UI Components
    private JPanel listPanel; // The container inside the scroll pane
    private JTextField inputField;
    
    // State
    private final Color BG_COLOR = new Color(90, 122, 232);
    private ArrayList<String> blackList; // Single list for everything

    public SettingsGUI(AppMonitor m, DataManager d) {
        this.monitor = m;
        this.dataManager = d;

        // Link directly to the single list in AppMonitor
        this.blackList = monitor.getBlackList();

        // 1. Window Setup
        setTitle("Distraction Settings");
        setSize(400, 600);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_COLOR);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 2. Header Panel (Title Only)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel titleLabel = new JLabel("Blocked Apps & Sites");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);


        // 3. The List Area (Middle)
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS)); 
        listPanel.setBackground(BG_COLOR);
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        scrollPane.setBackground(BG_COLOR);
        scrollPane.getViewport().setBackground(BG_COLOR);
        
        // Speed up scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Padding container
        JPanel scrollContainer = new JPanel(new BorderLayout());
        scrollContainer.setBackground(BG_COLOR);
        scrollContainer.setBorder(new EmptyBorder(0, 20, 0, 20)); // Side margins
        scrollContainer.add(scrollPane, BorderLayout.CENTER);

        add(scrollContainer, BorderLayout.CENTER);


        // 4. Input Area (Bottom)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 20, 0));

        inputField = new JTextField(20);
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Placeholder text logic
        TextPrompt placeholder = new TextPrompt("Add an App or Keyword...", inputField);
        placeholder.changeAlpha(0.75f);
        placeholder.changeStyle(Font.ITALIC);

        // Add Listener to Enter Key
        inputField.addActionListener(e -> addItem());

        // Enter Button 
        JButton addBtn = new JButton("✓");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        addBtn.setBackground(new Color(255, 255, 255));
        addBtn.setForeground(BG_COLOR);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> addItem());

        bottomPanel.add(inputField);
        bottomPanel.add(addBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        // Initial Load
        refreshList();
        setVisible(true);
    }

    // --- LOGIC METHODS ---

    private void refreshList() {
        listPanel.removeAll(); // Clear current items
        
        for (String item : blackList) {
            listPanel.add(createListItem(item));
            listPanel.add(Box.createVerticalStrut(10)); // Gap between items
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addItem() {
        String input = inputField.getText().trim();
        
        // Don't add empty strings or duplicates
        if (!input.isEmpty() && !blackList.contains(input)) {
            blackList.add(input);       // Add to list
            dataManager.saveData();  // Save to file
            inputField.setText("");     // Clear box
            refreshList();              // Update UI
        }
    }

    private void deleteItem(String text) {
        blackList.remove(text);     // Remove from list
        dataManager.saveData();  // Save to file
        refreshList();              // Update UI
    }

    // --- UI HELPER METHODS ---

    private JPanel createListItem(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(400, 45)); // Fixed height
        panel.setBackground(Color.WHITE); 
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Padding left

        // The Text
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(BG_COLOR);
        panel.add(label, BorderLayout.CENTER);

        // The Delete Button [X]
        JButton deleteBtn = new JButton("X");
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(200, 60, 60)); // Red X
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setPreferredSize(new Dimension(45, 45));
        
        deleteBtn.addActionListener(e -> deleteItem(text));

        panel.add(deleteBtn, BorderLayout.EAST);

        return panel;
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