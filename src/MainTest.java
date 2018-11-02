import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 */
public class MainTest {

    private static String[] input = { "WM001 N11W5 05:11:50", "WM003 N7E50 05:31:50",
            "WM004 N11E5 06:11:50", "WM002 S3E2 05:11:55" };
//
//    private static String[] input = { "WM001 N1 03:01:00", "WM002 E58 06:00:00"};


    public static void main(String[] args) {
        List<Order> orders = Parser.parse(input);
        orders.sort((o1, o2) -> o1.getOrderTime().compareTo(o2.getOrderTime()));
        System.out.println(orders);

        // Set<OrderFinal> results = new TreeSet<>((o1, o2) -> o1.getCurrentPathScore() -
        // o2.getCurrentPathScore());

        Machine drone = Machine.getMachine();

        // start working
        // list all possible combinations of the delivery orders.

        List<Integer> source = new ArrayList<>();
        source.add(1);
        source.add(2);
        source.add(3);
        System.out.println(results);
        List<List<Order>> combinations = combination(orders);
        System.out.println(results);
//        List<List<Order>> feasibleSolutions = new ArrayList<>();
//        for (List<Order> list : combinations) {
//            if (checkFeasibility(list, drone.getCurrentTime())) {
//                feasibleSolutions.add(list);
//            }
//        }
//        System.out.println(feasibleSolutions);

        List<List<Order>> feasibleSolutions = combination2(orders, drone.getCurrentTime());
//        System.out.println("Combination 2: "+ combinations2);
//        assert(feasibleSolutions.size() == combinations2.size());
//        for (List<Order> os: feasibleSolutions){
//            List<Order> os2 = combinations2.get(feasibleSolutions.indexOf(os));
//            assert(os.size() == os2.size());
//            for (int i = 0; i<os.size(); i++){
//                assert(os.get(i).getId().equals(os2.get(i).getId()));
//            }
//        }

      System.out.println(feasibleSolutions);

        // for each calculate score
        List<Integer> scores = new ArrayList<>(); // index match the feasibleSolutions index

        for (List<Order> solution : feasibleSolutions) {
            int score = 0;
            LocalTime time = drone.getCurrentTime();
            for (Order o : solution) {
                score += OrderFinal.calculateStatus(o, time);
                time = time.plusSeconds((long) Math.ceil(o.getDistance() * 2 * 60));
            }
            scores.add(score);
        }
        System.out.println(scores);
        int maxScore = scores.stream().max(Integer::compare).get();
        int index = scores.indexOf(maxScore);
        System.out.println("Max Score: "+maxScore +" Index: "+index+" Source: "+feasibleSolutions.get(index));
    }

    private static List<List<Order>> results = new ArrayList<>();

    private static List<List<Order>> combination(List<Order> source) {
        List<List<Order>> intermediateResults = new ArrayList<>();
        if (source.isEmpty()) {
            return new ArrayList<>();
        }
        else if (source.size() == 1) {
            List<Order> result = new ArrayList<>(source);
            results.add(result);
            intermediateResults.add(result);
            return intermediateResults;
        }
        else {
            for (Order i : source) {
                List<Order> subList = new ArrayList<>(source); // all orders - current - delivered ones
                subList.remove(i);
                List<List<Order>> result = combination(subList);
                for (List<Order> r : result) {
                    r.add(0, i);
                }
                intermediateResults.addAll(result);
            }
            return intermediateResults;
        }
    }

    private static List<List<Order>> combination2(List<Order> source, LocalTime time) {
        List<List<Order>> intermediateResults = new ArrayList<>();
        if (source.isEmpty()) {
            return new ArrayList<>();
        }
        else if (source.size() == 1 && (!source.get(0).getOrderTime().isAfter(time))) {//only add the feasible solution
            List<Order> result = new ArrayList<>(source);
            results.add(result);
            intermediateResults.add(result);
            return intermediateResults;
        }
        else {
            for (Order i : source) {
                if (i.getOrderTime().isAfter(time)){//not feasible
                    continue;
                }
                List<Order> subList = new ArrayList<>(source); // all orders - current - delivered ones
                subList.remove(i);
                time = time.plusSeconds((long)Math.ceil(i.getDistance()*2*60));
                List<List<Order>> result = combination2(subList, time);
                for (List<Order> r : result) {
                    r.add(0, i);
                }
                intermediateResults.addAll(result);
            }
            return intermediateResults;
        }
    }

    private static boolean checkFeasibility(List<Order> orders, LocalTime time) {
        for (Order o : orders) {
            if (o.getOrderTime().isAfter(time)) {
                return false;
            }
            time = time.plusSeconds((long) Math.ceil(o.getDistance() * 2 * 60));
        }
        return true;
    }

    private static List<List<Order>> combination1(List<Order> source, LocalTime time) {
        List<List<Order>> intermediateResults = new ArrayList<>();
        if (source.isEmpty()) {
            return new ArrayList<>();
        }
        else if (source.size() == 1 && (source.get(0).getOrderTime().compareTo(time) <= 0)) {
            List<Order> result = new ArrayList<>(source);
            results.add(result);
            intermediateResults.add(result);
            return intermediateResults;
        }
        else {
            List<Order> subList0 = new ArrayList<>(source);
            List<Order> filteredOrder = subList0.stream().filter(o -> o.getOrderTime().compareTo(time) <= 0)
                    .collect(Collectors.toList());
            for (Order i : filteredOrder) {
                List<Order> subList = new ArrayList<>(source); // all orders - current - delivered ones
                subList.remove(i);
                List<List<Order>> result = combination1(subList,
                        time.plusSeconds((long) Math.ceil(i.getDistance() * 2 * 60)));
                for (List<Order> r : result) {
                    r.add(0, i);
                }
                intermediateResults.addAll(result);
            }
            return intermediateResults;
        }
    }
}
