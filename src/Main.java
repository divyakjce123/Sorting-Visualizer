import algorithms.AlgorithmDefinitions;
import controller.PlaybackController;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import model.SortingAlgorithm;
import model.Step;
import ui.*;
import utils.SoundManager;

public class Main {
    private JFrame frame;
    private VisualizerPanel vizPanel;
    private PseudocodePanel pseudoPanel;
    private JComboBox<String> algoCombo;
    private JSpinner sizeSpinner;
    private JButton btnUseInput, btnRandomize, btnPlayPause, btnStepBack, btnStepFwd, btnReset;
    private JToggleButton btnSoundToggle;
    private JSlider speedSlider;
    private JLabel lblMessage, lblStats, lblLevel, narrationLabel, compValBest, compValAvg, compValWorst, spaceValLabel;
    private JProgressBar progressBar;
    private JTextField inputField;
    private JTextArea detailsArea;
    private JComboBox<String> compareComboRight;
    private JButton btnCompareRight;

    private java.util.List<Step> steps = new ArrayList<>();
    private int[] currentArray = new int[]{5, 2, 3, 1, 4};
    private PlaybackController controller;
    private final Map<String, SortingAlgorithm> algorithms = new LinkedHashMap<>();

    public Main() {
        registerAlgorithms();
        SwingUtilities.invokeLater(this::buildUI);
    }

    private void registerAlgorithms() {
        addAlg(new AlgorithmDefinitions.BubbleSortRecorder());
        addAlg(new AlgorithmDefinitions.InsertionSortRecorder());
        addAlg(new AlgorithmDefinitions.SelectionSortRecorder());
        addAlg(new AlgorithmDefinitions.MergeSortRecorder());
        addAlg(new AlgorithmDefinitions.QuickSortRecorder());
        addAlg(new AlgorithmDefinitions.HeapSortRecorder());
        addAlg(new AlgorithmDefinitions.ShellSortRecorder());
        addAlg(new AlgorithmDefinitions.CountingSortRecorder());
    }
    private void addAlg(SortingAlgorithm a) { algorithms.put(a.getName(), a); }

    private JButton makeButton(String text, int width) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(40, 140, 255));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(width, 36));
        b.setMaximumSize(new Dimension(width, 36));
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void buildUI() {
        frame = new JFrame("DSA Game: Sorting Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int frameW = 1366, frameH = 820;
        frame.setSize(frameW, frameH);

        double leftRatio = 4.2 / 14.6, centerRatio = 6.4 / 14.6;
        int leftWidth = (int) (frameW * leftRatio);
        int centerWidth = (int) (frameW * centerRatio);
        int rightWidth = frameW - leftWidth - centerWidth;
        frame.setLayout(new BorderLayout());

        // LEFT PANEL
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(new Color(20, 20, 26));
        left.setBorder(new EmptyBorder(14, 20, 14, 20));
        left.setPreferredSize(new Dimension(leftWidth, frameH));

        JLabel title = new JLabel("Sorting Visualizer");
        title.setFont(new Font("SansSerif", Font.BOLD, 24)); title.setForeground(new Color(230, 230, 230));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(title); left.add(Box.createVerticalStrut(20));

        JLabel lblAlg = new JLabel("Algorithm:"); lblAlg.setForeground(new Color(200, 200, 200));
        lblAlg.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblAlg);
        
        algoCombo = new JComboBox<>(algorithms.keySet().toArray(new String[algorithms.size()]));
        
        algoCombo.setMaximumSize(new Dimension(300, 34)); algoCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(algoCombo); left.add(Box.createVerticalStrut(12));

        JLabel lblSize = new JLabel("Array Size:"); lblSize.setForeground(new Color(200, 200, 200));
        lblSize.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblSize);
        sizeSpinner = new JSpinner(new SpinnerNumberModel(8, 2, 80, 1));
        sizeSpinner.setMaximumSize(new Dimension(120, 28)); sizeSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(sizeSpinner); left.add(Box.createVerticalStrut(12));

        JLabel lblCustom = new JLabel("Custom array (comma separated):"); lblCustom.setForeground(new Color(200, 200, 200));
        lblCustom.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblCustom);
        inputField = new JTextField();
        inputField.setMaximumSize(new Dimension(300, 30)); inputField.setPreferredSize(new Dimension(300, 30));
        inputField.setBackground(Color.WHITE); inputField.setForeground(Color.BLACK); inputField.setCaretColor(Color.BLACK);
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(inputField);
        left.add(Box.createVerticalStrut(25));

        JPanel buttonsStack = new JPanel();
        buttonsStack.setOpaque(false);
        buttonsStack.setLayout(new BoxLayout(buttonsStack, BoxLayout.Y_AXIS));
        buttonsStack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUseInput = makeButton("Use Input", 280); btnUseInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRandomize = makeButton("Randomize", 280); btnRandomize.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStepBack = makeButton("<< Step Back", 280); btnStepBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlayPause = makeButton("Play / Pause", 280); btnPlayPause.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStepFwd = makeButton("Step Forward >>", 280); btnStepFwd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReset = makeButton("Reset", 280); btnReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Sound Toggle
        btnSoundToggle = new JToggleButton("Sound: ON");
        btnSoundToggle.setFocusPainted(false);
        btnSoundToggle.setBackground(new Color(255, 165, 0));
        btnSoundToggle.setForeground(Color.BLACK);
        btnSoundToggle.setMaximumSize(new Dimension(280, 36));
        btnSoundToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSoundToggle.setSelected(true);

        buttonsStack.add(btnUseInput); buttonsStack.add(Box.createVerticalStrut(10));
        buttonsStack.add(btnRandomize); buttonsStack.add(Box.createVerticalStrut(15));
        buttonsStack.add(btnStepBack); buttonsStack.add(Box.createVerticalStrut(10));
        buttonsStack.add(btnPlayPause); buttonsStack.add(Box.createVerticalStrut(10));
        buttonsStack.add(btnStepFwd); buttonsStack.add(Box.createVerticalStrut(10));
        buttonsStack.add(btnReset); buttonsStack.add(Box.createVerticalStrut(10));
        buttonsStack.add(btnSoundToggle);
        left.add(buttonsStack); left.add(Box.createVerticalStrut(30));

        JLabel lblSpeed = new JLabel("Speed:"); lblSpeed.setForeground(new Color(200, 200, 200));
        lblSpeed.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblSpeed);
        speedSlider = new JSlider(10, 1000, 300); speedSlider.setMaximumSize(new Dimension(300, 36));
        speedSlider.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(speedSlider); left.add(Box.createVerticalStrut(15));

        lblLevel = new JLabel("Level: Learner"); lblLevel.setForeground(new Color(220, 220, 220));
        lblLevel.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblLevel); left.add(Box.createVerticalStrut(6));
        progressBar = new JProgressBar(0, 100); progressBar.setMaximumSize(new Dimension(300, 18));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(progressBar); left.add(Box.createVerticalStrut(15));

        lblMessage = new JLabel("Ready"); lblMessage.setForeground(new Color(210, 210, 210));
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT); left.add(lblMessage); left.add(Box.createVerticalStrut(6));
        lblStats = new JLabel("<html><center>Comparisons: 0<br/>Swaps: 0<br/>Step: 0/0</center></html>", SwingConstants.CENTER);
        lblStats.setForeground(new Color(210, 210, 210)); lblStats.setAlignmentX(Component.CENTER_ALIGNMENT);
        left.add(lblStats);

        // CENTER PANEL
        vizPanel = new VisualizerPanel(); vizPanel.setPreferredSize(new Dimension(centerWidth, frameH));
        narrationLabel = new JLabel(" "); narrationLabel.setForeground(new Color(255, 255, 255));
        narrationLabel.setBorder(new EmptyBorder(8, 8, 8, 8)); narrationLabel.setFont(narrationLabel.getFont().deriveFont(Font.BOLD, 13f));
        JPanel center = new JPanel(new BorderLayout()); center.setBackground(new Color(20, 20, 26));
        center.add(vizPanel, BorderLayout.CENTER); center.add(narrationLabel, BorderLayout.SOUTH);

        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout()); right.setBackground(new Color(30, 30, 36));
        right.setBorder(new EmptyBorder(10, 14, 10, 14)); right.setPreferredSize(new Dimension(rightWidth, frameH));
        JPanel rightTop = new JPanel(); rightTop.setLayout(new BoxLayout(rightTop, BoxLayout.Y_AXIS)); rightTop.setOpaque(false);

        JLabel rightTitle = new JLabel("Pseudocode & Details"); rightTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        rightTitle.setForeground(new Color(230, 230, 230)); rightTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightTop.add(rightTitle); rightTop.add(Box.createVerticalStrut(8));

        pseudoPanel = new PseudocodePanel(); pseudoPanel.setPreferredSize(new Dimension(rightWidth - 40, 220));
        pseudoPanel.setAlignmentX(Component.LEFT_ALIGNMENT); rightTop.add(pseudoPanel); rightTop.add(Box.createVerticalStrut(15));

        JLabel defHeader = new JLabel("Definition:"); defHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        defHeader.setForeground(new Color(230, 230, 230)); defHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightTop.add(defHeader); rightTop.add(Box.createVerticalStrut(6));

        detailsArea = new JTextArea(); detailsArea.setEditable(false); detailsArea.setLineWrap(true); detailsArea.setWrapStyleWord(true);
        detailsArea.setBackground(new Color(26, 26, 26)); detailsArea.setForeground(new Color(230, 230, 230));
        detailsArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)), new EmptyBorder(8, 8, 8, 8)));
        detailsArea.setPreferredSize(new Dimension(rightWidth - 40, 80)); detailsArea.setMaximumSize(new Dimension(rightWidth - 40, 80));
        detailsArea.setAlignmentX(Component.LEFT_ALIGNMENT); rightTop.add(detailsArea); rightTop.add(Box.createVerticalStrut(20));

        JLabel timeTitle = new JLabel("Time Complexity:"); timeTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        timeTitle.setForeground(Color.WHITE); timeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightTop.add(timeTitle); rightTop.add(Box.createVerticalStrut(5));

        JPanel tablePanel = new JPanel(new GridLayout(2, 3)); tablePanel.setBackground(new Color(40, 40, 40));
        tablePanel.setBorder(new LineBorder(new Color(100, 100, 100), 1)); tablePanel.setMaximumSize(new Dimension(rightWidth - 20, 80));
        tablePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        Color gridColor = new Color(100, 100, 100);
        compValBest = new JLabel("", SwingConstants.CENTER); compValBest.setForeground(Color.WHITE);
        compValAvg = new JLabel("", SwingConstants.CENTER); compValAvg.setForeground(Color.WHITE);
        compValWorst = new JLabel("", SwingConstants.CENTER); compValWorst.setForeground(Color.WHITE);
        tablePanel.add(createCell("Best", gridColor)); tablePanel.add(createCell("Average", gridColor)); tablePanel.add(createCell("Worst", gridColor));
        tablePanel.add(createValueCell(compValBest, gridColor)); tablePanel.add(createValueCell(compValAvg, gridColor)); tablePanel.add(createValueCell(compValWorst, gridColor));
        rightTop.add(tablePanel); rightTop.add(Box.createVerticalStrut(20));

        JLabel spaceHeader = new JLabel("Space Complexity:"); spaceHeader.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        spaceHeader.setForeground(new Color(230, 230, 230)); spaceHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightTop.add(spaceHeader); rightTop.add(Box.createVerticalStrut(6));
        spaceValLabel = new JLabel(""); spaceValLabel.setForeground(new Color(230, 230, 230));
        JPanel spaceBox = new JPanel(new BorderLayout()); spaceBox.setOpaque(false);
        spaceBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)), new EmptyBorder(8, 8, 8, 8)));
        spaceBox.add(spaceValLabel, BorderLayout.CENTER); spaceBox.setPreferredSize(new Dimension(rightWidth - 40, 40));
        spaceBox.setMaximumSize(new Dimension(rightWidth - 40, 40)); spaceBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightTop.add(spaceBox); rightTop.add(Box.createVerticalStrut(30));

        JPanel compareSection = new JPanel(); compareSection.setLayout(new BoxLayout(compareSection, BoxLayout.Y_AXIS));
        compareSection.setOpaque(false); compareSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        compareSection.setBorder(new MatteBorder(1, 0, 0, 0, new Color(80, 80, 80)));
        JLabel compareLbl = new JLabel("Compare Current Algorithm With:"); compareLbl.setForeground(new Color(220, 220, 220));
        compareLbl.setAlignmentX(Component.LEFT_ALIGNMENT); compareComboRight = new JComboBox<>();
        compareComboRight.setMaximumSize(new Dimension(rightWidth - 40, 30)); compareComboRight.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCompareRight = new JButton("COMPARE ALGORITHMS"); btnCompareRight.setBackground(new Color(255, 165, 0));
        btnCompareRight.setForeground(Color.BLACK); btnCompareRight.setOpaque(true); btnCompareRight.setBorderPainted(false);
        btnCompareRight.setFont(new Font("SansSerif", Font.BOLD, 14)); btnCompareRight.setMaximumSize(new Dimension(rightWidth - 40, 40));
        btnCompareRight.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); btnCompareRight.setAlignmentX(Component.LEFT_ALIGNMENT);
        compareSection.add(Box.createVerticalStrut(15)); compareSection.add(compareLbl);
        compareSection.add(Box.createVerticalStrut(5)); compareSection.add(compareComboRight);
        compareSection.add(Box.createVerticalStrut(10)); compareSection.add(btnCompareRight);
        rightTop.add(compareSection); right.add(rightTop, BorderLayout.NORTH);

        frame.add(left, BorderLayout.WEST); frame.add(center, BorderLayout.CENTER); frame.add(right, BorderLayout.EAST);

        // Wire Events
        btnUseInput.addActionListener(e -> useInputArray());
        btnRandomize.addActionListener(e -> randomizeArray());
        btnPlayPause.addActionListener(e -> togglePlayPause());
        btnStepFwd.addActionListener(e -> { if (controller != null) controller.stepForward(); });
        btnStepBack.addActionListener(e -> { if (controller != null) controller.stepBack(); });
        btnReset.addActionListener(e -> resetController());
        speedSlider.addChangeListener(e -> { if (controller != null) controller.setDelay(speedSlider.getValue()); });
        
        btnSoundToggle.addActionListener(e -> {
            if (btnSoundToggle.isSelected()) {
                btnSoundToggle.setText("Sound: ON");
                btnSoundToggle.setBackground(new Color(255, 165, 0));
                SoundManager.setMuted(false);
            } else {
                btnSoundToggle.setText("Sound: OFF");
                btnSoundToggle.setBackground(Color.GRAY);
                SoundManager.setMuted(true);
            }
        });

        algoCombo.addActionListener(e -> buildStepsAndController());
        sizeSpinner.addChangeListener(e -> randomizeArray());
        btnCompareRight.addActionListener(e -> {
            String a = (String) algoCombo.getSelectedItem(); String b = (String) compareComboRight.getSelectedItem();
            if (a == null || b == null || a.equals(b)) { JOptionPane.showMessageDialog(frame, "Select a different algorithm to compare."); return; }
            showCompareModal(a, b);
        });

        for (String name : algorithms.keySet()) compareComboRight.addItem(name);
        algoCombo.addActionListener(e -> {
            String current = (String) algoCombo.getSelectedItem(); compareComboRight.removeAllItems();
            for (String nm : algorithms.keySet()) if (!nm.equals(current)) compareComboRight.addItem(nm);
            buildStepsAndController();
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE -> togglePlayPause();
                    case KeyEvent.VK_LEFT -> { if (controller != null) controller.stepBack(); }
                    case KeyEvent.VK_RIGHT -> { if (controller != null) controller.stepForward(); }
                }
            }
        });
        frame.setFocusable(true);
        randomizeArray(); buildStepsAndController();
        frame.setLocationRelativeTo(null); frame.setVisible(true);
    }

    private JLabel createCell(String text, Color borderColor) {
        JLabel l = new JLabel("<html><b>" + text + "</b></html>", SwingConstants.CENTER);
        l.setForeground(Color.WHITE); l.setOpaque(true); l.setBackground(new Color(50, 50, 55));
        l.setBorder(new LineBorder(borderColor, 1)); return l;
    }
    private JLabel createValueCell(JLabel existingLabel, Color borderColor) {
        existingLabel.setOpaque(true); existingLabel.setBackground(new Color(30, 30, 30));
        existingLabel.setBorder(new LineBorder(borderColor, 1)); return existingLabel;
    }

    private void togglePlayPause() { if (controller == null) return; if (controller.isRunning()) { controller.pause(); btnPlayPause.setText("Play"); } else { controller.play(); btnPlayPause.setText("Pause"); } }
    private void resetController() { if (controller == null) return; controller.pause(); controller.reset(); btnPlayPause.setText("Play"); updateLevel(); narrationLabel.setText(" "); }
    private void randomizeArray() { int n = (Integer) sizeSpinner.getValue(); Random r = new Random(); currentArray = new int[n]; for (int i = 0; i < n; i++) currentArray[i] = 1 + r.nextInt(Math.max(5, n * 3)); vizPanel.setArray(currentArray); buildStepsAndController(); }
    private void useInputArray() {
        String text = inputField.getText().trim(); if (text.isEmpty()) { lblMessage.setText("Input empty"); return; }
        String[] parts = text.split(","); int[] arr = new int[parts.length];
        try { for (int i = 0; i < parts.length; i++) arr[i] = Integer.parseInt(parts[i].trim()); } 
        catch (NumberFormatException ex) { lblMessage.setText("Invalid input — numbers only, comma separated"); return; }
        currentArray = arr; vizPanel.setArray(currentArray); sizeSpinner.setValue(arr.length); buildStepsAndController(); lblMessage.setText("Using custom array"); updateLevel();
    }

    private void buildStepsAndController() {
        String algName = (String) algoCombo.getSelectedItem(); SortingAlgorithm alg = algorithms.get(algName); if (alg == null) return;
        pseudoPanel.setLines(alg.getPseudocodeLines()); detailsArea.setText(alg.getDefinition());
        if (compValBest != null) compValBest.setText(getBestForAlgorithm(algName));
        if (compValAvg != null) compValAvg.setText(alg.getAvgTime());
        if (compValWorst != null) compValWorst.setText(alg.getWorstTime());
        if (spaceValLabel != null) spaceValLabel.setText(alg.getSpace());
        steps = alg.generateSteps(currentArray);
        
        // FIX: Call stopAll() to kill any running success animation
        if (controller != null) controller.stopAll();
        
        controller = new PlaybackController(vizPanel, pseudoPanel, lblMessage, lblStats, progressBar, steps, currentArray);
        controller.setDelay(speedSlider.getValue());
        lblStats.setText("<html><center>Comparisons: 0<br/>Swaps: 0<br/>Step: 0/" + steps.size() + "</center></html>");
        if (!steps.isEmpty() && steps.get(0).snapshot != null) vizPanel.setArray(steps.get(0).snapshot);
        updateLevel();
    }

    private String getBestForAlgorithm(String algName) {
        return switch (algName) {
            case "Bubble Sort", "Insertion Sort" -> "O(n)";
            case "Selection Sort" -> "O(n²)";
            case "Merge Sort", "Quick Sort", "Heap Sort" -> "O(n log n)";
            case "Shell Sort" -> "Varies";
            case "Counting Sort" -> "O(n + K)";
            default -> "-";
        };
    }

    private void updateLevel() {
        int n = currentArray.length; String alg = (String) algoCombo.getSelectedItem();
        String baseLevel; if (n <= 8) baseLevel = "Level: Learner"; else if (n <= 20) baseLevel = "Level: Apprentice"; else baseLevel = "Level: Challenger";
        boolean isAdvanced = "Quick Sort".equals(alg) || "Merge Sort".equals(alg) || "Heap Sort".equals(alg);
        lblLevel.setText(baseLevel + (isAdvanced ? " - Advanced Algorithm" : ""));
    }

    private void showCompareModal(String aName, String bName) {
        SortingAlgorithm A = algorithms.get(aName), B = algorithms.get(bName); if (A == null || B == null) return;
        String[] cols = new String[]{"Aspect", aName, bName};
        String[][] rows = new String[][]{
            {"Definition", A.getDefinition(), B.getDefinition()}, {"Average Time", A.getAvgTime(), B.getAvgTime()},
            {"Worst Time", A.getWorstTime(), B.getWorstTime()}, {"Space", A.getSpace(), B.getSpace()},
            {"Typical Use", A.getTypicalUse(), B.getTypicalUse()}, {"Advantages", A.getAdvantages(), B.getAdvantages()},
            {"Disadvantages", A.getDisadvantages(), B.getDisadvantages()}
        };
        JTable table = new JTable(rows, cols); table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); table.setRowHeight(40);
        for (int c = 0; c < table.getColumnCount(); c++) table.getColumnModel().getColumn(c).setCellRenderer(new HTMLCellRenderer());
        JScrollPane sp = new JScrollPane(table); sp.setPreferredSize(new Dimension(Math.min(900, frame.getWidth() - 200), Math.min(420, frame.getHeight() - 200)));
        JOptionPane.showMessageDialog(frame, sp, "Compare: " + aName + " vs " + bName, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
        SwingUtilities.invokeLater(Main::new);
    }
}