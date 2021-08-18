package anagram;

import java.awt.Font;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class AnagramGUI extends JFrame {

    private JPanel contentPane;
    private JTextField inputField;

    public AnagramGUI() {
        // Frame and content pane
        super("Anagram Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 390, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Set system theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Title text
        JLabel titleLabel = new JLabel("ANAGRAM MAKER");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titleLabel.setBounds(10, 11, 424, 20);
        contentPane.add(titleLabel);

        // Input instructions label
        JLabel inputLabel = new JLabel("Please input a word or phrase to find anagrams of:");
        inputLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        inputLabel.setBounds(10, 40, 354, 14);
        contentPane.add(inputLabel);

        // Input text field
        inputField = new JTextField();
        inputField.setBounds(10, 58, 209, 20);
        contentPane.add(inputField);
        inputField.setColumns(10);

        // Text field displaying list of anagrams
        JTextArea anagramDisplay = new JTextArea();
        anagramDisplay.setEditable(false);
        anagramDisplay.setFont(new Font("Monospaced", Font.PLAIN, 13));
        anagramDisplay.setBounds(10, 123, 354, 227);
        contentPane.add(anagramDisplay);

        // Find anagrams button
        JButton generateButton = new JButton("Find anagrams!");
        generateButton.setFont(new Font("Consolas", Font.PLAIN, 11));
        generateButton.setBounds(10, 89, 130, 23);
        contentPane.add(generateButton);
        generateButton.addActionListener((event) -> {
            // Construct new AnagramMaker object
            AnagramMaker anagramMaker = new AnagramMaker();
            // Load dictionary words into word list
            anagramMaker.loadDictionary("src/dictionary.txt");

            // Get user input from text field
            String input = inputField.getText().toUpperCase().replaceAll("\\s", "");
            // Generate all permutations of the input word/phrase
            Set<String> permutations = anagramMaker.generatePermutations(input);
            // Eliminate permutations not found in the dictionary
            List<String> anagrams = anagramMaker.getValidAnagrams(permutations);
            // Alphabetize the list of anagrams
            Collections.sort(anagrams);

            // Display all valid anagrams
            if (anagrams.size() > 0) {
                StringBuilder allAnagrams = new StringBuilder();
                for (String s : anagrams) {
                    allAnagrams.append("\n" + s);
                }
                anagramDisplay.setText("All valid anagrams (in alphabetical order):" + allAnagrams.toString());
            } else {
                anagramDisplay.setText("No valid anagrams!");
            }
        });
    }

}
