package anagram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AnagramMaker {

    public static void main(String[] args) {
        System.out.println("Please input a word or phrase:");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase().replaceAll("\\s", "");
        scanner.close();

        List<String> dictionary = getDictionary("src/anagram/dictionary.txt");
        List<String> anagrams = filterWords(generatePermutations(input), dictionary);
        Collections.sort(anagrams);

        if (anagrams.size() > 0) {
            System.out.println("\nAll valid anagrams (in alphabetical order):");
            for (String s : anagrams) {
                System.out.println(s);
            }
        } else {
            System.out.println("\nNo valid anagrams!");
        }
    }

    public static Set<String> generatePermutations(String word) {
        // I use a HashSet so that duplicate permutations are not added
        Set<String> permutations = new HashSet<String>();

        // Base case - a word with only 1 character only has 1 permutation (itself)
        if (word.length() <= 1) {
            permutations.add(word);
            return permutations;
        }

        for (int i = 0; i < word.length(); i++) {
            String subword = new StringBuilder(word).deleteCharAt(i).toString();
            Set<String> subPermutations = generatePermutations(subword);
            for (String subPerm : subPermutations) {
                permutations.add(word.charAt(i) + subPerm);
            }
        }
        return permutations;
    }

    public static List<String> filterWords(Set<String> permutations, List<String> dictionary) {
        List<String> validAnagrams = new ArrayList<String>();
        for (String perm : permutations) {
            if (dictionary.contains(perm)) {
                validAnagrams.add(perm);
            }
        }
        return validAnagrams;
    }

    public static List<String> getDictionary(String filepath) {
        List<String> dictionary = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filepath)));
            String word = br.readLine();
            while (word != null) {
                dictionary.add(word.toUpperCase());
                word = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

}
