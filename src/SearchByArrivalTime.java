import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class SearchByArrivalTime {

    private static ArrayList<String> allTripsFromFile;
    private static ArrayList<String[]> relevantTripsFromFile;
    public static final int ARRIVAL_TIME_INDEX = 1;

    SearchByArrivalTime (String filename) {

        allTripsFromFile = new ArrayList<String>();
        SimpleDateFormat myDateFormat = new SimpleDateFormat("hh:mm:ss");

        try {
            Scanner scanFile = new Scanner(new File(filename));
            scanFile.nextLine(); //Skip metadata line
            String maxTimeAsString = "23:59:59";
            Date maxTimeAsDate = myDateFormat.parse(maxTimeAsString);

            while (scanFile.hasNextLine()) {

                String currentLineFromStopTimes = scanFile.nextLine();
                String[] currentLineFromStopTimesParseArray = currentLineFromStopTimes.split(",");
                Date time = myDateFormat.parse(currentLineFromStopTimesParseArray[1]);

                if (time.getTime() <= maxTimeAsDate.getTime()) {
                    allTripsFromFile.add(currentLineFromStopTimes);
                }
            }

        } catch (FileNotFoundException | ParseException e) {
            JOptionPane.showMessageDialog(null,"Stop times file not found." +
                    "Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public static ArrayList<String[]> getTrips(String inputTimeAsString, ArrayList<String> allTrips) {

        relevantTripsFromFile = new ArrayList<String[]>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        for (int i = 0; i < allTrips.size(); i++) {

            String currentLine = allTrips.get(i);
            String[] currentLineParseArray = currentLine.split(",");

            try {
                Date currentTime = dateFormat.parse(currentLineParseArray[ARRIVAL_TIME_INDEX]);
                Date inputTimeAsDate = dateFormat.parse(inputTimeAsString);
                if (currentTime.getTime() == inputTimeAsDate.getTime()) {
                    relevantTripsFromFile.add(currentLineParseArray);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null,"Could not match given time with" +
                        " bank of all possible times", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return relevantTripsFromFile;
    }


    public static boolean getDetails(String arrivalTimeGiven) {

        ArrayList<String[]> relevantTripsFromFileSorted;
        relevantTripsFromFile = new ArrayList<String[]>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");

        for (int i = 0; i < allTripsFromFile.size(); i++) {

            String currentLine = allTripsFromFile.get(i);
            String[] currentLineParseArray = currentLine.split(",");

            try {
                Date currentTime = dateFormat.parse(currentLineParseArray[ARRIVAL_TIME_INDEX]);
                Date inputTimeAsDate = dateFormat.parse(arrivalTimeGiven);
                if (currentTime.getTime() == inputTimeAsDate.getTime()) {
                    relevantTripsFromFile.add(currentLineParseArray);
                }
            } catch (Exception e){
                JOptionPane.showMessageDialog(null,"Could not match given time with" +
                        " bank of all possible times", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Perform insertion sort on the array list of string arrays (where string array holds each line of stop_times)
        relevantTripsFromFileSorted = insertionSortResultsByTripID(relevantTripsFromFile);

        if (!relevantTripsFromFileSorted.isEmpty()) {

            //Convert data to display string using string builder
            StringBuilder fullTripDetails = new StringBuilder();

            for (int i = 0; i < relevantTripsFromFileSorted.size() ; i++) {

                fullTripDetails.append("Trip ").append(i + 1).append(":\n\n");
                fullTripDetails.append("Trip ID: ").append(relevantTripsFromFileSorted.get(i)[0]).append("\n");
                fullTripDetails.append("Arrival Time: ").append(relevantTripsFromFileSorted.get(i)[1]).append("\n");
                fullTripDetails.append("Departure Time: ").append(relevantTripsFromFileSorted.get(i)[2]).append("\n");
                fullTripDetails.append("Stop ID: ").append(relevantTripsFromFileSorted.get(i)[3]).append("\n");
                fullTripDetails.append("Stop Sequence: ").append(relevantTripsFromFileSorted.get(i)[4]).append("\n");
                fullTripDetails.append("Stop Headsign: ").append(relevantTripsFromFileSorted.get(i)[5]).append("\n"); //Metadata doesn't exist for any
                fullTripDetails.append("Pickup Type: ").append(relevantTripsFromFileSorted.get(i)[6]).append("\n");
                fullTripDetails.append("Dropoff Type: ").append(relevantTripsFromFileSorted.get(i)[7]).append("\n");
                fullTripDetails.append("Shape Dist Travelled: ").append(relevantTripsFromFileSorted.get(i)[8]).append("\n\n");
            }

            String fullTripInfo = fullTripDetails.toString();

            ShowScrollingText showTripInfo = new ShowScrollingText(fullTripInfo , "Trips");

        }
        else {
            JOptionPane.showMessageDialog(null,"No trips match your search criteria",
                    "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    public static ArrayList<String[]> insertionSortResultsByTripID(ArrayList<String[]> resultsToBeSorted){

        int tripID;
        String[] tempCopyI;

        for (int i = 1; i < resultsToBeSorted.size(); i++) {

            tripID = Integer.parseInt(resultsToBeSorted.get(i)[0]);
            tempCopyI = resultsToBeSorted.get(i);
            int j = i-1;

            while ((j>=0) && ((Integer.parseInt(resultsToBeSorted.get(j)[0])) > tripID)) {
                resultsToBeSorted.set(j + 1, resultsToBeSorted.get(j));
                j = j-1;
            }

            resultsToBeSorted.set(j + 1, tempCopyI);
        }

        return resultsToBeSorted;
    }
}
