import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/*
 * Usage instructions:
 * 
 * The Trip class represents a sequence of cities being visited in
 * a particular order.  Each city is represented with an integer between
 * 1 and numCities inclusive.
 * 
 * Construct a Trip as follows:
 *     Trip myTrip = new Trip(numCities);
 * 
 * To put a city into the sequence do the following:
 *     myTrip.chooseNextCity(city);
 * 
 * To remove the last city that was put into the sequence
 * do the following:
 *     myTrip.unchooseLastCity();
 * 
 * To see if the trip is a valid trip with all of the cities,
 * call isPossible() and pass in a directed graph that indicates
 * which cities are connected.
 *     myTrip.isPossible(graph)
 * 
 * To determine the cost of a trip, call
 *     myTrip.tripCost(graph)
 * 
 * There are some other handy routines in the below you might
 * want to check out while implementing PA5.
 */
public class Trip {

    // Constructor that initializes the citiesLeft with all cities 1 through
    // numCities inclusive
    public Trip(int numCities) {
        visitOrder = new ArrayList<>();
        citiesLeft = new TreeSet<>();
        for (int i = 1; i <= numCities; i++) {
            citiesLeft.add(i);
        }
    }

    // Copy another trip into this object.
    public void copyOtherIntoSelf(Trip tripSofar) {
        // Making a copy of the set and list in this data structure.
        citiesLeft = new TreeSet<>(tripSofar.citiesLeft);
        visitOrder = new ArrayList<>(tripSofar.visitOrder);
    }

    // Has the given city been put in the sequence yet or is it
    // still available? Return true if it is not in the sequence
    // and is still available.
    public boolean isCityAvailable(int city) {
        return citiesLeft.contains(city);
    }

    // Put the given city next in the trip list.
    public void chooseNextCity(int next) {
        assert isCityAvailable(next);
        visitOrder.add(next);
        citiesLeft.remove(next);
    }

    // Take off the last city from the list.
    public void unchooseLastCity() {
        assert visitOrder.size() > 0;
        int city = visitOrder.get(visitOrder.size() - 1);
        visitOrder.remove(visitOrder.size() - 1);
        citiesLeft.add(city);
    }

    // Return true if (1) all cities 1 thru numnodes are visited,
    // (2) there is an edge between adjacent cities in the trip,
    // and (3) there is a path from the last city in the trip to the first.
    public boolean isPossible(DGraph graph) {
        double cost = tripCost(graph);
        if (visitOrder.size() == graph.getNumNodes()
                && cost != Double.MAX_VALUE) {
            return true;
        } else {
            return false;
        }
    }

    // Returns Double.MAXVALUE if can't find connections.
    // FIXME: this whole method is not clear and needs rewritten
    public double tripCost(DGraph graph) {
        double cost = 0;
        if (visitOrder.size() == 0) {
            return Double.MAX_VALUE;
        } else if (visitOrder.size() == 1) {
            return 0;
        }
        int prevCity = visitOrder.get(0);

        // visit second city through last and add trip weights
        for (int i = 1; i < visitOrder.size(); i++) {
            double distance = graph.getWeight(prevCity, visitOrder.get(i));
            prevCity = visitOrder.get(i);
            if (distance < 0) {
                cost = Double.MAX_VALUE;
                break;
            } else {
                cost += distance;
            }
        }

        // Add in distance between last city and first, but only if all
        // of the cities are in the trip.
        if (citiesLeft.isEmpty()) {
            double goingHome = graph.getWeight(
                visitOrder.get(visitOrder.size() - 1),
                visitOrder.get(0));
            if (goingHome < 0 || cost == Double.MAX_VALUE) {
                cost = Double.MAX_VALUE;
            } else {
                cost = cost + goingHome;
            }
        }
        return cost;
    }

    // Provide an ordered list of all of the cities left.
    public List<Integer> citiesLeft() {
        List<Integer> retval = new ArrayList<>();
        for (Integer city : citiesLeft) {
            retval.add(city);
        }
        return retval;
    }

    // Print out the trip and its total cost.
    public String toString(DGraph graph) {
        String str = "";
        str += "cost = " + String.format("%.1f", tripCost(graph));
        str += ", visitOrder = " + visitOrder;
        return str;
    }

    // Print out the trip and its total cost.
    public String toString() {
        String str = "";
        str += "visitOrder = " + visitOrder;
        str += ", citiesLeft = " + citiesLeft;
        return str;
    }

    private List<Integer> visitOrder;
    private TreeSet<Integer> citiesLeft;

}
