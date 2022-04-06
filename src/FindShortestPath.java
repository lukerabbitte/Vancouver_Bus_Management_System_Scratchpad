import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FindShortestPath {

    private EdgeWeightedDigraph myGraph;

    FindShortestPath (String filename){
        myGraph = initialiseMyGraph(filename); // let's add all vertices from stops.txt to initial graph.
        myGraph = addTransferEdges(); // let's add edges from transfers.txt to the graph
        myGraph = addStopTimesEdges(); // let's add edges from stop_times.txt to the graph
    }

    public static boolean getShortestPath() {

        String filename = "stops.txt";
        FindShortestPath sp = new FindShortestPath(filename);


        return false;
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
                    "not found. Read terminal for stack trace");
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
                fromVertex = Integer.parseInt(currentLineParse[0]);
                toVertex = Integer.parseInt(currentLineParse[1]);
                transferType = Integer.parseInt(currentLineParse[2]);
                minTransferTime = Double.parseDouble(currentLineParse[3]);

                if (transferType==0)
                    weight = 2;
                else if (transferType==2)
                    weight = minTransferTime/100;

                DirectedEdge currentEdge = new DirectedEdge(fromVertex, toVertex, weight);
                myGraph.addEdge(currentEdge);
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Transfers file not found." +
                    "Read terminal for stack trace");
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

                currentTripID = Integer.parseInt(currentLineParse[0]);
                currentStopID = Integer.parseInt(currentLineParse[3]);
                currentStopSequenceNumber = Integer.parseInt(currentLineParse[4]);

                nextTripID = Integer.parseInt(currentLineParse[0]);
                nextStopID = Integer.parseInt(currentLineParse[3]);
                nextStopSequenceNumber = Integer.parseInt(currentLineParse[4]);

                if ( (currentTripID == nextTripID) && (currentStopSequenceNumber == (nextStopSequenceNumber-1)) ) {
                    DirectedEdge currentEdge = new DirectedEdge(currentStopID, nextStopID, weight);
                    myGraph.addEdge(currentEdge);
                }

                currentLine = nextLine;
                currentLineParse = nextLineParse;
            }
        }
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Stop times file not found." +
                    "Read terminal for stack trace");
            e.printStackTrace();
        }
        return myGraph;
    }




}
