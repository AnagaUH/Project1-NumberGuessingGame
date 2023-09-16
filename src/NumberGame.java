import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class NumberGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name: ");
        String username = scanner.nextLine();
        if (username.trim().isEmpty()) {
            username = "'No Name'";
        }
        System.out.println("\nHello " + username + "!" + """

                Please choose an option by entering its corresponding number.
                
                1. Start Guessing Game
                2. See Top 5 High Scores
                3. See all recorded scores
                4. Exit""");

        try {
            int userMenuInput = Integer.parseInt(scanner.nextLine());
            switch (userMenuInput) {
                case 1 -> { // Start guessing game
                    GuessingGame(username);
                    System.exit(0);
                    scanner.close();
                }
                case 2 -> { // Print top 5 scores
                    printScores(5);
                    scanner.close();
                }
                case 3 -> { // Print all scores
                    printScores(-1);
                    scanner.close();
                }
                case 4 -> { // Exit application
                    System.out.println("Exiting...");
                    System.exit(0);
                }
                default -> System.out.println("Please enter a valid option.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid option.\nExiting...");
        }
    }
    static void GuessingGame(String name) { // Recursive guessing game
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int userGuess;
        int choice;
        int target = random.nextInt(100) + 1;
        boolean guessing = true;
        int guesses = 0;

        System.out.println("\nThe number guessing game will start!");
        System.out.println("""
                \nInstructions:
                Enter a number between 1 and 100, then I will tell you whether the number I'm thinking of is 'Higher' or 'Lower' than your guess.
                Guess the number in the least amount of tries.
                Enter -1 to exit.

                Enter a number:\s""");

        while (guessing) { // Loop guessing
            userGuess = Integer.parseInt(scanner.nextLine());
            guesses++;
            if (userGuess == -1) { // Quit
                System.out.println("\nQuitting Game...");
                guesses = -1;
                guessing = false;
            } else if (userGuess > 100 || userGuess < 1) { // Handle out of bounds
                System.out.println("Please enter a number 1-100:\n");
            } else if (userGuess == target) { // Player wins
                System.out.println("\nYou got it in '" + guesses + "' guesses!");
                addNewScore(name, guesses);
                printScores(3);
                guessing = false;
                System.out.println("""
                        \nWould you like to play again?
                        1. Yes
                        2. No""");
                Scanner restart = new Scanner(System.in);
                choice = Integer.parseInt(restart.nextLine());
                if (choice == 1) { // Recursion
                    GuessingGame(name);
                }
            } else if (userGuess < target) {
                System.out.println("\nHigher");
                System.out.println("Enter a number: ");
            } else {
                System.out.println("\nLower");
                System.out.println("Enter a number: ");
            }
        }
    }
    static void printScores(int scoresShown) { // Read HighScores.txt and print out its contents
        List<PlayerScore> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("HighScores.txt"))) { // Read HighScores.txt and add its integers to List scores
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    try {
                        String name = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        PlayerScore entry = new PlayerScore(name, score);
                        scores.add(entry);
                    } catch (NumberFormatException e) {
                        System.out.println("HighScores.txt has invalid score");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
        insertionSort(scores); // Call insertionSort to sort List scores
        if (scoresShown == -1) { // Print all scores stored
            System.out.println("\nHere are all recorded scores: ");
            for (PlayerScore score : scores) {
                System.out.println(score.getName() + ": " + score.getScore());
            }
        }
        else { // Print only the selected amount of top scores
            System.out.println("\nHere are the top " + scoresShown + " scores:");
            for (int i = 0; i < scoresShown && i < scores.size(); i++) {
                System.out.println(scores.get(i).getName() + ": " + scores.get(i).getScore());
            }
        }
    }
    static void addNewScore(String name, int score){ // Add a new score to HighScores.txt
        try {
            FileWriter fw = new FileWriter("HighScores.txt", true); // Append new score to HighScores.txt
            fw.write(name + "," + score);
            fw.write("\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Error writing to file");
            System.exit(0);
        }
    }
    public static List<PlayerScore> insertionSort(List<PlayerScore> array) { // Insertion sort for Lists
        int n = array.size();
        for (int i = 1; i < n; i++) {
            int keyScore = array.get(i).getScore();
            String keyName = array.get(i).getName();
            int j = i - 1;
            while (j >= 0 && array.get(j).getScore() > keyScore) {
                array.set(j + 1, array.get(j));
                j--;
            }
            PlayerScore key = new PlayerScore(keyName, keyScore);
            array.set(j + 1, key);
        }
        return array;
    }
}
class PlayerScore {
    private String name;
    private int score;

    public PlayerScore() {
        this.name = "---";
        this.score = -1;
    }
    public PlayerScore(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {return name;}
    public int getScore() {return score;}
    public void setName(String name) {this.name = name;}
    public void setScore(int score) {this.score = score;}

    @Override
    public String toString() {
        return name + ": " + score;
    }
}