import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Parser {

    static List<Order> parse(String[] input){
        List<Order> orders = new ArrayList<>();

        for (String s: input){
            String[] source = s.trim().split("\\s");
            Order o = new Order(source[0], source[1], source[2]);
            orders.add(o);
        }
        return orders;
    }

}
