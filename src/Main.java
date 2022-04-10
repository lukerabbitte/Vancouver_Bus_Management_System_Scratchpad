import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.MinPQ;
import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        boolean userWantsToQuit = false;
        boolean invalidInput = false;
        int optionChoice = -1;

        while (!userWantsToQuit) {

            optionChoice = showMenuAndReturnChoice(optionChoice);

            //Default optionChoice value will be -1 if there is some error. Otherwise 0, 1, 2, or 3.
            if (optionChoice == 0) {
                userWantsToQuit = true;
                JOptionPane.showMessageDialog(null,"You have successfully exited" +
                        " the Vancouver Bus Management System Interface.");
            }
            else if (optionChoice == 1) {
                callFindShortestPath();
            }
            else if (optionChoice == 2) {
                callSearchForStop();
            }
            else if (optionChoice == 3) {
                callSearchByArrivalTime();
            }
            else
                invalidInput = true;
        }

        if (invalidInput)
            JOptionPane.showMessageDialog(null,"There has been an error. Try again.",
                    "Error!", JOptionPane.ERROR_MESSAGE);
    }

    private static int showMenuAndReturnChoice(int choice) {

        JFrame f = new JFrame();
        JOptionPane.showMessageDialog(f,"Welcome to the Vancouver Bus Management System");

        Object[] menuOptions = {"Quit", "Find Shortest Path Between Two Stops", "Search For A Stop", "Search By Arrival Time"};
        Object selectedObject = JOptionPane.showInputDialog(f, "Please choose an option from the menu",
                "Menu", JOptionPane.PLAIN_MESSAGE, null, menuOptions, menuOptions[0]);
        String selectionString = selectedObject.toString();

        if (selectionString.equals("Quit"))
            choice = 0;
        else if (selectionString.equals("Find Shortest Path Between Two Stops"))
            choice = 1;
        else if (selectionString.equals("Search For A Stop"))
            choice = 2;
        else if (selectionString.equals("Search By Arrival Time"))
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

    private static void callSearchForStop() {

        SearchForStop myStopSearch = new SearchForStop("stops.txt");
        boolean successfulCall = false;

        while(!successfulCall) {
            String stopName = JOptionPane.showInputDialog("Enter the name or the first few characters" +
                    " of the bus stop you are searching for");
            if(myStopSearch.displayStopDetails(stopName.toUpperCase()))
                successfulCall = true;
        }
    }

    private static void callSearchByArrivalTime() {

        boolean successfulCall = false;
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setLenient(false);
        SearchByArrivalTime mySearchByArrivalTime = new SearchByArrivalTime("stop_times.txt");

        while(!successfulCall) {

            String arrivalTime = JOptionPane.showInputDialog("Enter the arrival time you want to find details for");

            try {
                timeFormat.parse(arrivalTime);
                if(mySearchByArrivalTime.getDetails(arrivalTime))
                    successfulCall = true;
            } catch(ParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid time format given",
                        "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}