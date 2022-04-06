import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.MinPQ;

import javax.swing.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        boolean validInput = false;
        int optionChoice = -1;

        while (!validInput) {

            optionChoice = showMenuAndReturnChoice(optionChoice);

            //Default optionChoice value will be -1 if there is some error. Otherwise 0, 1, 2, or 3.
            if (optionChoice == 0) {
                validInput = true;
                JOptionPane.showMessageDialog(null,"You have successfully exited" +
                        " the Vancouver Bus Management System Interface.");
            }
            else if (optionChoice == 1) {
                validInput = true;
                callFindShortestPath();
            }
            else if (optionChoice == 2) {
                validInput = true;
                callSearchForStop();
            }
            else if (optionChoice == 3) {
                validInput = true;
                callSearchByArrivalTime();
            }
            else
                JOptionPane.showMessageDialog(null,"There has been an error. Try again.");
        }

    }

    private static int showMenuAndReturnChoice(int choice) {

        JFrame f = new JFrame();
        JOptionPane.showMessageDialog(f,"Welcome to the Vancouver Bus Management System");

        Object[] menuOptions = {"Quit", "Find Shortest Path Between Two Stops", "Search For A Stop", "Search By Arrival Time"};
        Object selectedObject = JOptionPane.showInputDialog(f, "Please choose an option from the menu", "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
        String selectionString = selectedObject.toString();

        if (selectionString=="Quit")
            choice = 0;
        else if (selectionString=="Find Shortest Path Between Two Stops")
            choice = 1;
        else if (selectionString=="Search For A Stop")
            choice = 2;
        else if (selectionString=="Search By Arrival Time")
            choice = 3;

        return choice;
    }

    private static void callFindShortestPath() {

        boolean successfulCall = false;

        while(!successfulCall) {
            if(FindShortestPath.getShortestPath()) {
                successfulCall = true;
            }
        }
    }

    public static void callSearchForStop() {

        SearchForStop myStopSearch = new SearchForStop("stops.txt");
        boolean validSearch = false;

        while(!validSearch) {
            System.out.println("Enter the stop you are looking for:\n");
            String inputStop = userInput.nextLine();
            if(myStopSearch.displayStopDetails(inputStop.toUpperCase()))
                validSearch = true;

        }

        userInput.close();
    }

}