/*
Author: Carlos Garcia
Description: This program receives user input for available work weeks and a .txt file, and solves
             the corresponding knapsack problem without repetitions. Finally, it outputs a summary
             that includes the expected profit and a list of best projects for the company to undergo.
             This algorithm runs in O(nW) time.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Knapsack {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the number of available employee work weeks: ");
        int workWeeks = input.nextInt();
        System.out.print("Enter the name of input file: ");
        String fileName = input.next();
        Scanner dataFile = new Scanner(new File(fileName));
        ArrayList<Project> projects = new ArrayList<>();
        ArrayList<Project> result = new ArrayList<>();

        // reads user input file and adds projects to the ArrayList
        while (dataFile.hasNextLine()) {
            String line = dataFile.nextLine();
            String[] split = line.split(" ");
            projects.add(new Project(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
        dataFile.close();

        int[][] K = new int[workWeeks + 1][projects.size() + 1];

        // calculates the max possible profit
        for (int j = 1; j < projects.size() + 1; j++) {
            for (int w = 0; w < workWeeks + 1; w++) {
                if (projects.get(j - 1).workWeeks > w) {
                    K[w][j] = K[w][j - 1];
                }
                else {
                    K[w][j] = Math.max(K[w - projects.get(j - 1).workWeeks][j - 1] + projects.get(j - 1).profit, K[w][j - 1]);
                }
            }
        }

        // determines which projects should be chosen
        int w = workWeeks;
        for (int j = projects.size(); j > 0; j--) {
            if (K[w][j] > K[w][j - 1]) {
                result.add(projects.get(j - 1));
                w -= projects.get(j - 1).workWeeks;
            }
        }

        // prints the results
        System.out.println("Number of projects available = " + projects.size());
        System.out.println("Available employee work weeks = " + workWeeks);
        System.out.println("Number of projects chosen = " + result.size());
        int totalProfit = 0;
        for (Project project : result) {
            totalProfit += project.profit;
        }
        System.out.println("Total profit: " + totalProfit);
        for (int i = result.size() - 1; i >= 0; i--) {
            System.out.println(result.get(i).projectName + " " + result.get(i).workWeeks + " " + result.get(i).profit);
        }
    }

    public static class Project {
        String projectName;
        int workWeeks;
        int profit;

        // constructor for projects given in the .txt file
        public Project(String name, int w, int j) {
            this.projectName = name;
            this.workWeeks = w;
            this.profit = j;
        }
    }
}
