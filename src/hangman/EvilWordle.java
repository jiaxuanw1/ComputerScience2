package hangman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class EvilWordle extends JPanel implements KeyListener {

    private final int BOX_SIZE = 60;
    private final int SPACING = BOX_SIZE / 5;
    private final int BORDER = BOX_SIZE / 2;

    private final List<String> allowedGuesses;
    private List<String> wordList = new ArrayList<String>();
    private List<String> wordsGuessed = new ArrayList<String>();
    private List<Character> lettersGuessed = new ArrayList<Character>();

    private String word = "ALOHA";

    private int currentGuessNumber;

    private boolean win;
    private boolean lose;
    private boolean tooShortDisplay;
    private boolean invalidWordDisplay;
    private boolean repeatWordDisplay;

    public static void main(String[] args) {
        EvilWordle wordle = new EvilWordle();

        JFrame frame = new JFrame("Evil Wordle");
        frame.add(wordle);
        frame.setSize(wordle.getPreferredSize());
        frame.pack();
        frame.addKeyListener(wordle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        wordle.newGame();

        Timer t = new Timer(1000 / 30, e -> frame.getComponent(0).repaint());
        t.start();
    }

    public EvilWordle() {
        setPreferredSize(new Dimension(410, 560));
        addKeyListener(this);
        wordList = loadDictionary("src/util/wordle-list.txt");
        allowedGuesses = loadDictionary("src/util/wordle-list.txt");
    }

    public void newGame() {
        currentGuessNumber = 0;
        wordsGuessed.clear();
        lettersGuessed.clear();
        win = false;
        lose = false;
        tooShortDisplay = false;
        invalidWordDisplay = false;
        repeatWordDisplay = false;

        word = wordList.get((int) (Math.random() * wordList.size()));
    }

    public void updateWordList(String guess) {

    }

    public String getColorSequence(String word, String guess) {
        StringBuilder colorSequence = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (guess.charAt(i) == word.charAt(i)) {
                colorSequence.append("g");
            } else if (word.indexOf(guess.charAt(i)) >= 0) {
                colorSequence.append("y");
            } else {
                colorSequence.append("b");
            }
        }
        return colorSequence.toString();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();

        for (int guessNum = 0; guessNum < 6; guessNum++) {
            // Generate color sequence of already-guessed word
            String colorSequence = null;
            if (guessNum < currentGuessNumber) {
                colorSequence = getColorSequence(word, wordsGuessed.get(guessNum));
            }

            for (int charNum = 0; charNum < 5; charNum++) {
                int x0 = BORDER + charNum * (BOX_SIZE + SPACING);
                int y0 = BORDER + guessNum * (BOX_SIZE + SPACING);

                if (guessNum < currentGuessNumber) {
                    if (colorSequence.charAt(charNum) == 'g') {
                        g.setColor(new Color(0, 170, 0)); // green
                    } else if (colorSequence.charAt(charNum) == 'y') {
                        g.setColor(new Color(200, 200, 0)); // yellow
                    } else {
                        g.setColor(new Color(50, 50, 50)); // gray
                    }
                } else {
                    g.setColor(Color.BLACK);
                    if (guessNum == currentGuessNumber) {

                    }
                }
                g.fillRect(x0, y0, BOX_SIZE, BOX_SIZE);

                g.setColor(Color.WHITE);
                if (guessNum < currentGuessNumber) {
                    String letter = wordsGuessed.get(guessNum).charAt(charNum) + "";
                    g.drawString(letter, x0 + (BOX_SIZE - fm.stringWidth(letter)) / 2,
                            y0 + (BOX_SIZE - fm.getHeight()) / 2);
                } else if (guessNum == currentGuessNumber && charNum < lettersGuessed.size()) {
                    String letter = lettersGuessed.get(charNum) + "";
                    g.drawString(letter, x0 + (BOX_SIZE - fm.stringWidth(letter)) / 2,
                            y0 + (BOX_SIZE - fm.getHeight()) / 2);
                }
            }
        }

        String message = null;
        if (win) {
            message = "You won in " + currentGuessNumber + " tries!\nPress ENTER to play again.";
        } else if (lose) {
            message = "You lose! The answer was: " + word + "\nPress ENTER to play again.";
        } else if (tooShortDisplay) {
            message = "Please guess a five-letter word!";
        } else if (invalidWordDisplay) {
            message = "Please guess a valid word!";
        } else if (repeatWordDisplay) {
            message = "Word has already been guessed!";
        }
        if (message != null) {
            g.setColor(Color.BLACK);
            String[] messageComponents = message.split("\n");
            for (int i = 0; i < messageComponents.length; i++) {
                int messageX = ((2 * BORDER + 5 * BOX_SIZE + 4 * SPACING) - fm.stringWidth(messageComponents[i])) / 2;
                int messageY = 2 * BORDER + 6 * BOX_SIZE + 6 * SPACING + i * fm.getHeight() * 2;
                g.drawString(messageComponents[i], messageX, messageY);
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c >= 'a' && c <= 'z') {
            if (lettersGuessed.size() < 5 && !win && !lose) {
                lettersGuessed.add(Character.toUpperCase(c));
                tooShortDisplay = false;
                invalidWordDisplay = false;
                repeatWordDisplay = false;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_BACK_SPACE:
                if (!lettersGuessed.isEmpty() && !win && !lose) {
                    lettersGuessed.remove(lettersGuessed.size() - 1);
                    tooShortDisplay = false;
                    invalidWordDisplay = false;
                    repeatWordDisplay = false;
                }
                break;
            case KeyEvent.VK_ENTER:
                // If the game is finished
                if (win || lose) {
                    newGame();
                }
                // If 5 letters have been guessed, enter guess word
                else if (lettersGuessed.size() == 5) {
                    StringBuilder guessWord = new StringBuilder();
                    for (char c : lettersGuessed) {
                        guessWord.append(c);
                    }
                    String guess = guessWord.toString();

                    // Guess is a valid word
                    if (allowedGuesses.contains(guess)) {
                        // Word has already been guessed
                        if (wordsGuessed.contains(guess)) {
                            repeatWordDisplay = true;
                        }
                        // New valid guess
                        else {
                            wordsGuessed.add(guess);
                            lettersGuessed.clear();

                            currentGuessNumber++;

                            // If guess is correct, win
                            if (guess.equals(word)) {
                                win = true;
                            }

                            // If 6 incorrect guesses have been made, lose
                            if (currentGuessNumber > 5 && !win) {
                                lose = true;
                            }
                        }

                    }
                    // Invalid word guessed
                    else {
                        invalidWordDisplay = true;
                    }

                }
                // Not enough letters guessed
                else {
                    tooShortDisplay = true;
                }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public List<String> loadDictionary(String filepath) {
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String word = br.readLine();
            while (word != null) {
                list.add(word.toUpperCase());
                word = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return list;
    }

}
