import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Scanner;

public class FindShortestPath {

    // Relevant metadata indices from transfers.txt
    private static final int FROM_INDEX = 0;
    private static final int TO_INDEX = 1;
    private static final int TRANSFER_TYPE_INDEX = 2;
    private static final int MIN_TRANSFER_TIME_INDEX = 3;

    //Relevant metadata indices from stop_times.txt
    private static final int TRIP_ID_INDEX = 0;
    private static final int STOP_ID_INDEX = 3;
    private static final int STOP_SEQ_INDEX = 4;

    private EdgeWeightedDigraph myGraph;
    private static HashMap<Integer, Integer> dijkstraMap = new HashMap<Integer, Integer>();
    private static HashMap<Integer, Integer> reverseDijkstraMap = new HashMap<Integer, Integer>();
    private int mapIndex = 0;

    private static int userSourceStopID = -1;
    private static int userDestinationStopID = -1;

    FindShortestPath(String filename) {
        myGraph = initialiseMyGraph(filename); // let's add all vertices from stops.txt to initial graph.
        myGraph = addTransferEdges(); // let's add edges from transfers.txt to the graph
        myGraph = addStopTimesEdges(); // let's add edges from stop_times.txt to the graph
    }

    public EdgeWeightedDigraph initialiseMyGraph(String filename) {

        if (filename == null) {
            return null;
        }
        File file = new File(filename);
        int numberOfVertices = -1;

        try {
            Scanner scanFile = new Scanner(file);

            while (scanFile.hasNextLine()) {
                numberOfVertices++; //except for line 0, each line in the file is a new stop/vertex
                scanFile.nextLine();
            }

            myGraph = new EdgeWeightedDigraph(numberOfVertices);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File used to initialise graph " +
                    "not found. Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
        }
        return myGraph;
    }


    public EdgeWeightedDigraph addTransferEdges() {

        int fromVertex = -1;
        int toVertex = -1;
        int transferType = -1;
        double minTransferTime = -1.0;
        double weight = -1.0;

        try {
            File file = new File("transfers.txt");
            Scanner scanFile = new Scanner(file);
            String skipMetadataLine = scanFile.nextLine();

            while (scanFile.hasNextLine()) {

                //Split current line of file into string array. Assign each datum to required variable.
                String currentLine = scanFile.nextLine();
                String[] currentLineParse = currentLine.split(",");
                fromVertex = Integer.parseInt(currentLineParse[FROM_INDEX]);
                toVertex = Integer.parseInt(currentLineParse[TO_INDEX]);
                transferType = Integer.parseInt(currentLineParse[TRANSFER_TYPE_INDEX]);

                if (dijkstraMap.get(fromVertex) == null) {
                    dijkstraMap.put(fromVertex, mapIndex);
                    reverseDijkstraMap.put(mapIndex, fromVertex); //Insert into reverse map so we can later lookup by value
                    mapIndex++;
                }

                if (dijkstraMap.get(toVertex) == null) {
                    dijkstraMap.put(toVertex, mapIndex);
                    reverseDijkstraMap.put(mapIndex, toVertex); //Insert into reverse map so we can later lookup by value

                    /*
                    Note the complexity: extra n space to store reverse map. However, we will later be able to lookup by
                    value in constant time rather than somehow trying to return key by value in the original
                    uni-directional hashmap dijkstraMap.
                    */

                    mapIndex++;
                }

                if (transferType == 0)
                    weight = 2;
                else if (transferType == 2) {
                    minTransferTime = Double.parseDouble(currentLineParse[MIN_TRANSFER_TIME_INDEX]);
                    weight = minTransferTime / 100;
                }

                DirectedEdge currentEdge = new DirectedEdge(dijkstraMap.get(fromVertex), dijkstraMap.get(toVertex), weight);
                myGraph.addEdge(currentEdge);
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Transfers file not found." +
                    "Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return myGraph;
    }

    public EdgeWeightedDigraph addStopTimesEdges() {

        int currentTripID = -1;
        int currentStopID = -1;
        int currentStopSequenceNumber = -1;
        int nextTripID = -1;
        int nextStopID = -1;
        int nextStopSequenceNumber = -1;
        double weight = 1.0;

        try {
            File file = new File("stop_times.txt");
            Scanner scanFile = new Scanner(file);
            String skipMetadataLine = scanFile.nextLine();

            String currentLine = scanFile.nextLine();
            String[] currentLineParse = currentLine.split(",");

            while (scanFile.hasNextLine()) {

                String nextLine = scanFile.nextLine();
                String[] nextLineParse = currentLine.split(",");

                currentTripID = Integer.parseInt(currentLineParse[TRIP_ID_INDEX]);
                currentStopID = Integer.parseInt(currentLineParse[STOP_ID_INDEX]);
                currentStopSequenceNumber = Integer.parseInt(currentLineParse[STOP_SEQ_INDEX]);

                nextTripID = Integer.parseInt(currentLineParse[TRIP_ID_INDEX]);
                nextStopID = Integer.parseInt(currentLineParse[STOP_ID_INDEX]);
                nextStopSequenceNumber = Integer.parseInt(currentLineParse[STOP_SEQ_INDEX]);

                if ((currentTripID == nextTripID) && (currentStopSequenceNumber == (nextStopSequenceNumber - 1))) {

                    if (dijkstraMap.get(currentStopID) == null) {
                        dijkstraMap.put(currentStopID, mapIndex);
                        reverseDijkstraMap.put(mapIndex, currentStopID); //Insert into reverse map so we can later lookup by value
                        mapIndex++;
                    }

                    if (dijkstraMap.get(nextStopID) == null) {
                        dijkstraMap.put(nextStopID, mapIndex);
                        reverseDijkstraMap.put(mapIndex, nextStopID); //Insert into reverse map so we can later lookup by value
                        mapIndex++;
                    }

                    DirectedEdge currentEdge = new DirectedEdge(dijkstraMap.get(currentStopID), dijkstraMap.get(nextStopID), weight);
                    myGraph.addEdge(currentEdge);
                }

                currentLine = nextLine;
                currentLineParse = nextLineParse;
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Stop times file not found." +
                    "Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return myGraph;
    }

    public static boolean getShortestPath() {

        boolean validInput = false;
        int from = -1; //This of course assumes no stop ID is negative
        int to = -1;
        double totalCost = -1.0;

        /*Initialise graph from stops.txt. Each vertex index is a mapIndex retrieved as the value for corresponding
        stopID keys */
        String filename = "stops.txt";
        FindShortestPath myShortestPath = new FindShortestPath(filename);

        //Retrieve first two stops
        while (!validInput) {

            String fromVertex = JOptionPane.showInputDialog("Enter the ID of the first stop");
            String toVertex = JOptionPane.showInputDialog("Enter the ID of the second stop");
            from = Integer.parseInt(fromVertex);
            to = Integer.parseInt(toVertex);

            //Check that there are edges adjacent to both source and destination vertices/stops
            if ((dijkstraMap.get(from) != null) && (dijkstraMap.get(to) != null)) {
                validInput = true;
            } else {
                JOptionPane.showMessageDialog(null, "One or both of the stops " +
                        "entered has no adjacent edges", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }

        //Retrieve the graph array index associated with the stopID given by user
        DijkstraSP myDijkstraGraph = new DijkstraSP(myShortestPath.myGraph, dijkstraMap.get(from));

        if (myDijkstraGraph.hasPathTo(dijkstraMap.get(to))) {

            totalCost = myDijkstraGraph.distTo(dijkstraMap.get(to));

            StringBuilder intermediateStopsAndCosts = new StringBuilder();
            Iterable<DirectedEdge> stopList = myDijkstraGraph.pathTo(dijkstraMap.get(to));

            intermediateStopsAndCosts.append("The total cost of this route is ").append(totalCost).append("\n\n");
            for (DirectedEdge stop : stopList) {

                int lookupFromStopIDKeyByAdjacencyListIndexValue = reverseDijkstraMap.get(stop.from());
                int lookupToStopIDKeyByAdjacencyListIndexValue = reverseDijkstraMap.get(stop.to());

                intermediateStopsAndCosts.
                        append("From Stop ID ").append(lookupFromStopIDKeyByAdjacencyListIndexValue).
                        append(" to Stop ID ").append(lookupToStopIDKeyByAdjacencyListIndexValue).
                        append(", the cost is ").append(stop.weight()).append("\n");
            }

            ShowScrollingText longMessageToDisplay = new ShowScrollingText(intermediateStopsAndCosts.toString(), "Intermediate Stops");
            return true;
        }
        return false;
    }
}