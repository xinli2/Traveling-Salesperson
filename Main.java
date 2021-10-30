/*
 * Author: Xin Li
 * Five parts
 * (1) read graph file
 * (2) Heristic approach
 * (3) Backtracking approach
 * (4) My own approach
 * (5) Time evaluation
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    /*
    * the method for main.
    * */
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(args[0]));
        String line = "";
        boolean cmtFlag = true;
        while (cmtFlag) {
            line = sc.nextLine();
            cmtFlag = line.startsWith("%");
        }

        String str[] = line.split("\\s+");
        int rows = Integer.valueOf(str[0].trim()).intValue();

        DGraph graph = new DGraph(rows);
        while (sc.hasNext()) {
            line = sc.nextLine();
            str = line.split("\\s+");
            int node1 = (Integer.valueOf(str[0].trim())).intValue();
            int node2 = (Integer.valueOf(str[1].trim())).intValue();
            double weight = (Double.valueOf(str[2].trim())).doubleValue();
            graph.addEdge(node1, node2, weight);
        }
        sc.close();

        // part one
        // part two
        if (args[1].equalsIgnoreCase("HEURISTIC"))
            System.out.println(heuristic(graph).toString(graph));

        // part three
        else if (args[1].equalsIgnoreCase("BACKTRACK"))
            System.out.println(backtrack(graph).toString(graph));

        // part four
        else if (args[1].equalsIgnoreCase("MINE"))
            System.out.println(myown(graph).toString(graph));

        // part five
        else if (args[1].equalsIgnoreCase("TIME"))
            time(graph);
    }

    /*
    * for part two
    * heuristic approach to the traveling salesperson
    *
    * @param the graph inputed
    * */
    public static Trip heuristic(DGraph graph) {
        Trip ret = new Trip(graph.getNumNodes());
        int city = 1;
        ret.chooseNextCity(1);
        for (int i = 2; i <= graph.getNumNodes(); i++) {
            double shotestDistance = Double.MAX_VALUE;
            int inx = 0;
            for (int j = 0; j < graph.getNeighbors(city).size(); j++) {
                if (ret.isCityAvailable(graph.getNeighbors(city).get(j))
                        && graph.getWeight(city, graph.getNeighbors(city).get(j)) < shotestDistance) {
                    inx = graph.getNeighbors(city).get(j);
                    shotestDistance = graph.getWeight(city, graph.getNeighbors(city).get(j));
                }
            }
            ret.chooseNextCity(inx);
            city = inx;
        }
        return ret;
    }

    /*
     * for part three
     * backtracking function
     *
     * @param the graph inputed
     * @param trip
     * @param min trip
     * */
    private static Trip backtrackingFunction(DGraph graph, Trip trip, Trip min) {
        if (trip.citiesLeft().isEmpty()) {
            if (min.tripCost(graph) > trip.tripCost(graph))
                min.copyOtherIntoSelf(trip);
        }
        else if (min.tripCost(graph) > trip.tripCost(graph)) {
            for (int i = 0; i < trip.citiesLeft().size(); i++) {
                trip.chooseNextCity(trip.citiesLeft().get(i));
                min = backtrackingFunction(graph, trip, min);
                trip.unchooseLastCity();
            }
        }
        return min;
    }

    /*
     * for part three
     * backtracking approach to the traveling salesperson
     *
     * @param the graph inputed
     * */
    private static Trip backtrack(DGraph graph) {
        Trip ret = new Trip(graph.getNumNodes());
        ret.chooseNextCity(1);
        ret = backtrackingFunction(graph, ret, new Trip(graph.getNumNodes()));
        return ret;
    }

    /*
     * for part four
     * my own approach to the traveling salesperson
     *
     * @param the graph inputed
     * */
    private static Trip myown(DGraph graph) {
        Trip ret = new Trip(graph.getNumNodes());
        for(int i = 1; i <= graph.getNumNodes(); i++) {
            Trip trip = new Trip(graph.getNumNodes());
            int city = 1;
            trip.chooseNextCity(1);
            for (int j = 2; j <= graph.getNumNodes(); j++) {
                double shotestDistance = Double.MAX_VALUE;
                int inx = 0;
                for (int k = 0; k < graph.getNeighbors(city).size(); k++) {
                    if (trip.isCityAvailable(graph.getNeighbors(city).get(k))
                            && graph.getWeight(city, graph.getNeighbors(city).get(k)) < shotestDistance) {
                        inx = graph.getNeighbors(city).get(k);
                        shotestDistance = graph.getWeight(city, graph.getNeighbors(city).get(k));
                    }
                }
                trip.chooseNextCity(inx);
                city = inx;
            }
            if (trip.tripCost(graph) < ret.tripCost(graph))
                ret.copyOtherIntoSelf(trip);
        }
        return ret;
    }

    /*
     * for part five
     * time evaluation for the approaches to the traveling salesperson
     *
     * @param the graph inputed
     * */
    private static void time(DGraph graph) {
        Trip trip = null;
        long start = 0;
        long end = 0;
        start = System.nanoTime();
        trip = heuristic(graph);
        end = System.nanoTime();
        System.out.println("heuristic: cost = " + trip.tripCost(graph)
                + ", " + ((end - start) / 1000000.0) + " milliseconds");
        start = System.nanoTime();
        trip = myown(graph);
        end = System.nanoTime();
        System.out.println("mine: cost = " + trip.tripCost(graph)
                + ", "
                + ((end - start) / 1000000.0) + " milliseconds");
        start = System.nanoTime();
        trip = backtrack(graph);
        end = System.nanoTime();
        System.out.println("backtrack: cost = " + trip.tripCost(graph)
                + ", " + ((end - start) / 1000000.0) + " milliseconds\n\n");
    }
}
