import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SearchForStop {

    private static final int STOP_ID_INDEX = 0;
    private static final int STOP_CODE_INDEX = 1;
    private static final int STOP_NAME_INDEX = 2;
    private static final int STOP_DESC_INDEX = 3;
    private static final int STOP_LAT_INDEX = 4;
    private static final int STOP_LON_INDEX = 5;
    private static final int ZONE_ID_INDEX = 6;
    private static final int STOP_URL_INDEX = 7;
    private static final int LOCATION_TYPE_INDEX = 8;
    private static final int PARENT_STATION_INDEX = 9;

    private boolean successfulCall = false;
    private HashMap<String, String> myMap;

    //Create ternary search tree storing string arrays with all the information needed per line
    TST<String> myTST;

    public SearchForStop(String filename) {

        myTST = new TST<String>();

        try {
            Scanner scanFile = new Scanner(new File(filename)); //Main class passes stops.txt as filename
            scanFile.nextLine(); //Skip metadata line
            myMap = new HashMap<String, String>();

            while (scanFile.hasNextLine()) {
                String currentStop = scanFile.nextLine();
                String[] currentStopDetails = currentStop.split(",");

                String stopID = currentStopDetails[STOP_ID_INDEX];
                String stopName = currentStopDetails[STOP_NAME_INDEX];

                //Use a string builder to move keywords to end of string before adding to TST
                StringBuilder adjustedStopName = new StringBuilder();
                if (adjustedStopName.substring(0, 8).equals("FLAGSTOP"))
                {
                    String flagstopAndTwoLetterKeywordSection = adjustedStopName.substring(0, 11);
                    adjustedStopName.delete(0, 12);
                    adjustedStopName.append(" " + flagstopAndTwoLetterKeywordSection);
                }
                else if (adjustedStopName.substring(0, 2).equals("NB") ||
                        adjustedStopName.substring(0, 2).equals("EB") ||
                        adjustedStopName.substring(0, 2).equals("SB") ||
                        adjustedStopName.substring(0, 2).equals("WB"))
                {
                    String twoLetterKeywordSection = adjustedStopName.substring(0, 2);
                    adjustedStopName.delete(0, 3);
                    adjustedStopName.append(" " + twoLetterKeywordSection);
                }

                String stopNameAdjusted = adjustedStopName.toString();
                //TST keys will be adjustedStopNames & values will be length 10 arrays containing metadata.
                myTST.put(stopNameAdjusted, stopID);

                StringBuilder fullStopDetails = new StringBuilder();
                fullStopDetails.append("Stop_ID: " + stopID + "\n");
                fullStopDetails.append("Stop Code: " + currentStopDetails[STOP_CODE_INDEX] + "\n");
                fullStopDetails.append("Stop Name: " + stopName + "\n");
                fullStopDetails.append("Stop Description: " + currentStopDetails[STOP_DESC_INDEX] + "\n");
                fullStopDetails.append("Stop Latitude: " + currentStopDetails[STOP_LAT_INDEX] + "\n");
                fullStopDetails.append("Stop Longitude: " + currentStopDetails[STOP_LON_INDEX] + "\n");
                fullStopDetails.append("Zone ID: " + currentStopDetails[ZONE_ID_INDEX] + "\n");
                fullStopDetails.append("Location Type: " + currentStopDetails[LOCATION_TYPE_INDEX] + "\n");
                fullStopDetails.append("Location Type: " + currentStopDetails[PARENT_STATION_INDEX] + "\n");

                String fullStopInfo = fullStopDetails.toString();
                myMap.put(stopID, fullStopInfo);
                successfulCall = true;
            }
        }

        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Stops file not found." +
                    "Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public boolean displayStopDetails(String inputString) {

        final boolean[] outputSuccessful = {false};

        myTST.keysWithPrefix(inputString).forEach((currentInfo) -> {
            JOptionPane.showMessageDialog(null,currentInfo);
            outputSuccessful[0] = true;
        });

        if (outputSuccessful[0]==false) {
            JOptionPane.showMessageDialog(null,"No matches have been found for the" +
                    "bus stop specified.");
        }

        return outputSuccessful[0];
    }
}
