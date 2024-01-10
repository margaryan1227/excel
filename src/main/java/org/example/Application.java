package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {
    private static final int LOG_ROW_HEIGHT = 20;
    private static final Properties properties = getProperties();
    private static final String WINDOW_TITLE = "window.title";
    private static final String INPUT = "window.inputLabel";
    private static final String START_BUTTON = "window.startButton";
    private static final String BROWSE_BUTTON = "window.browseButton";
    private static final String FONT = "Segoe UI";
    private JTextField inputFileField;

    public static void main(String[] args) {
        if (args.length == 0) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            Application app = new Application();
            app.createAndShowGUI();
        } else {
            XmlResolver.main(args);
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame(properties.getProperty(WINDOW_TITLE));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Font labelFont = new Font(FONT, Font.PLAIN, 12);

        JPanel panel = new JPanel();
        JPanel inputPanel = new JPanel(new GridLayout(9, 2, 10, 10));

        inputPanel.add(createLabel(properties.getProperty(INPUT), labelFont));
        JPanel filePanel = new JPanel(new BorderLayout());
        inputFileField = createTextField();
        filePanel.add(inputFileField, BorderLayout.CENTER);
        JButton browseButton = new JButton(properties.getProperty(BROWSE_BUTTON));
        browseButton.addActionListener(new BrowseButtonListener());
        filePanel.add(browseButton, BorderLayout.EAST);
        inputPanel.add(filePanel);

        JButton startButton = new JButton(properties.getProperty(START_BUTTON));
        startButton.addActionListener(new StartButtonListener(inputFileField.getText()));
        JPanel startButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        startButtonPanel.add(startButton);
        inputPanel.add(new JPanel());
        inputPanel.add(startButtonPanel);

        panel.add(inputPanel, BorderLayout.NORTH);

        frame.setSize(600, 450);

        frame.getContentPane().add(panel);
        centerFrameOnScreen(frame);
        frame.setVisible(true);
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        ClassLoader classLoader = Application.class.getClassLoader();
        InputStream input = classLoader.getResourceAsStream("application.properties");
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    private static void centerFrameOnScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();

        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;

        frame.setLocation(x, y);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);

        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(15);
        textField.setEditable(true);
        textField.setPreferredSize(new Dimension(15, LOG_ROW_HEIGHT));
        return textField;
    }

    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedDirectory = fileChooser.getSelectedFile().getAbsolutePath();
                inputFileField.setText(selectedDirectory);
            }
        }
    }

    private static class StartButtonListener implements ActionListener {
        private final String inputPath;

        @Override
        public void actionPerformed(ActionEvent e) {
            XmlResolver.main(new String[]{inputPath});
        }

        StartButtonListener(String inputPath) {
            this.inputPath = inputPath;
        }
    }
}