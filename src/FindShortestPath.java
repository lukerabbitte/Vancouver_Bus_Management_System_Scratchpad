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
    private static HashMap<Integer, Boolean> vertexEncountered = new HashMap<Integer, Boolean>(); /* adds efficiency
                                as vertices with no adjacent edges are immediately discounted from Dijsktra search */

    FindShortestPath (String filename){
        myGraph = initialiseMyGraph(filename); // let's add all vertices from stops.txt to initial graph.
        myGraph = addTransferEdges(); // let's add edges from transfers.txt to the graph
        myGraph = addStopTimesEdges(); // let's add edges from stop_times.txt to the graph
    }

    public static boolean getShortestPath() {

        String filename = "stops.txt";
        FindShortestPath myShortestPath = new FindShortestPath(filename);
        double totalCost = -1.0;

        int fromVertex = (vertexPair().getKey());
        int toVertex = (vertexPair().getValue());

        //TODO this is where we could search for vertices by their map index. I'm going to just search through our
        //TODO adjacency list representation.

        DijkstraSP myDijkstraGraph = new DijkstraSP(myShortestPath.myGraph, fromVertex);

        if (myDijkstraGraph.hasPathTo(toVertex)) {

            totalCost = myDijkstraGraph.distTo(toVertex);
            JOptionPane.showMessageDialog(null, "Total cost of route between two stops: " + totalCost);

            StringBuilder intermediateStopsAndCosts = new StringBuilder();
            Iterable<DirectedEdge> stopList = myDijkstraGraph.pathTo(toVertex);
            for (DirectedEdge stop: stopList) {
                intermediateStopsAndCosts.append("From Stop ID ").append(stop.from()).append(" to Stop ID ")
                        .append(stop.to()).append(", the cost is ").append(stop.weight()).append("\n");
            }

            /* Perhaps JOptionPane is not the best for displaying long streams of output like in this case.
               Alternative could be to save this string to a file? */
            JOptionPane.showMessageDialog(null, intermediateStopsAndCosts.toString());
            return true;
        }
        return false;
    }

    private static AbstractMap.SimpleEntry<Integer, Integer> vertexPair() {

        boolean validInput = false;
        int from = -1; //This of course assumes no stop ID is negative
        int to = -1;

        while (!validInput) {

            String fromVertex = JOptionPane.showInputDialog("Enter the ID of the first stop");
            String toVertex = JOptionPane.showInputDialog("Enter the ID of the second stop");
            from = Integer.parseInt(fromVertex);
            to = Integer.parseInt(toVertex);

            if ( (vertexEncountered.get(from) != null) && (vertexEncountered.get(to) != null) ) {
                validInput = true;
            }
            else {
                JOptionPane.showMessageDialog(null, "One of the stops " +
                        "entered has no adjacent edges", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return new AbstractMap.SimpleEntry<Integer, Integer>(from, to);
    }


    public EdgeWeightedDigraph initialiseMyGraph(String filename) {

        if (filename == null) {
            return null;
        }
        File file = new File(filename);
        int numberOfVertices = -1;

        try {
            Scanner scanFile = new Scanner(file);

            while(scanFile.hasNextLine()) {
                numberOfVertices++; //except for line 0, each line in the file is a new stop/vertex
                scanFile.nextLine();
            }

            myGraph = new EdgeWeightedDigraph(numberOfVertices);
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"File used to initialise graph " +
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

            while(scanFile.hasNextLine()) {

                //Split current line of file into string array. Assign each datum to required variable.
                String currentLine = scanFile.nextLine();
                String[] currentLineParse = currentLine.split(",");
                fromVertex = Integer.parseInt(currentLineParse[FROM_INDEX]);
                toVertex = Integer.parseInt(currentLineParse[TO_INDEX]);
                transferType = Integer.parseInt(currentLineParse[TRANSFER_TYPE_INDEX]);

                if (transferType==0)
                    weight = 2;
                else if (transferType==2) {
                    minTransferTime = Double.parseDouble(currentLineParse[MIN_TRANSFER_TIME_INDEX]);
                    weight = minTransferTime / 100;
                }

                DirectedEdge currentEdge = new DirectedEdge(fromVertex, toVertex, weight);
                myGraph.addEdge(currentEdge);
                vertexEncountered.put(fromVertex, true);
                vertexEncountered.put(toVertex, true);
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Transfers file not found." +
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

            while(scanFile.hasNextLine()) {

                String nextLine = scanFile.nextLine();
                String[] nextLineParse = currentLine.split(",");

                currentTripID = Integer.parseInt(currentLineParse[TRIP_ID_INDEX]);
                currentStopID = Integer.parseInt(currentLineParse[STOP_ID_INDEX]);
                currentStopSequenceNumber = Integer.parseInt(currentLineParse[STOP_SEQ_INDEX]);

                nextTripID = Integer.parseInt(currentLineParse[TRIP_ID_INDEX]);
                nextStopID = Integer.parseInt(currentLineParse[STOP_ID_INDEX]);
                nextStopSequenceNumber = Integer.parseInt(currentLineParse[STOP_SEQ_INDEX]);

                if ( (currentTripID == nextTripID) && (currentStopSequenceNumber == (nextStopSequenceNumber-1)) ) {
                    DirectedEdge currentEdge = new DirectedEdge(currentStopID, nextStopID, weight);
                    myGraph.addEdge(currentEdge);
                    vertexEncountered.put(currentStopID, true);
                    vertexEncountered.put(nextStopID, true);
                }

                currentLine = nextLine;
                currentLineParse = nextLineParse;
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Stop times file not found." +
                    "Read terminal for stack trace", "Error!", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return myGraph;
    }
}
