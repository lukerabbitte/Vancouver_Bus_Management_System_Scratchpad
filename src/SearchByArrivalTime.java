import java.util.ArrayList;

public class SearchByArrivalTime {






    public static boolean getDetails(String arrivalTime) {

        ArrayList<String[]> results = new ArrayList<>();
        results 
        
        return false;
    }


    public static ArrayList<String[]> sortResultsByTripID (ArrayList<String[]> resultsToBeSorted){

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
