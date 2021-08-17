package anagram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

public class AnagramMaker {

    private List<String> dictionary = new ArrayList<String>();

    /**
     * Generates all the possible permutations (without repeats) of the specified
     * String.
     * 
     * @param word the String to generate permutations of
     * @return set containing the permutations
     */
    public Set<String> generatePermutations(String word) {
        // I use a set so that duplicate permutations are not added
        Set<String> permutations = new HashSet<String>();

        // Base case - a word with 0 or 1 character only has 1 permutation (itself)
        if (word.length() <= 1) {
            permutations.add(word);
            return permutations;
        }

        // For each character in the word, keep it at the front and recursively generate
        // all permutations of the remaining characters
        for (int i = 0; i < word.length(); i++) {
            String subword = new StringBuilder(word).deleteCharAt(i).toString();
            Set<String> subPermutations = generatePermutations(subword);
            for (String subPerm : subPermutations) {
                permutations.add(word.charAt(i) + subPerm);
            }
        }

        // Return the set of permutations
        return permutations;
    }

    /**
     * Receives a list of String permutations and returns a list containing only the
     * permutations that are valid words found in the dictionary word list.
     * 
     * @param permutations set containing all permutations to check
     * @return list containing only the permutations that are valid words
     */
    public List<String> getValidAnagrams(Set<String> permutations) {
        // Initialize new list to store only valid anagrams
        List<String> validAnagrams = new ArrayList<String>();

        for (String perm : permutations) {
            // Each permutation is only added if found in the dictionary
            if (dictionary.contains(perm)) {
                validAnagrams.add(perm);
            }
        }

        // Return the list of only valid anagrams
        return validAnagrams;
    }

    /**
     * Reads a specified dictionary text file and loads every word into the
     * dictionary word list. The text file should be formatted such that each word
     * is on its own line, with no empty lines. All words in the returned list are
     * fully capitalized.
     * 
     * @param filepath the path of the dictionary text file
     */
    public void loadDictionary(String filepath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            // Add each line of text as new element in dictionary
            String word = br.readLine();
            while (word != null) {
                dictionary.add(word.toUpperCase());
                word = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to read dictionary file.");
        }
    }

}
