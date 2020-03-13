import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
// TODO: rename to DeliveredOrder
// TODO: MACHINE = drone; parser = inputParser ; mainTest = main
public class OrderFinal {

    private Order order;

    private LocalTime departureTime;

    private int status;// 1 promoter, 0 neutral, -1 detractor

    private LocalTime nextDepartureTime;

    private List<OrderFinal> previousDeliver = new ArrayList<>();

    public OrderFinal(Order order, LocalTime departureTime, OrderFinal...prevOrders) {
        this.order = order;
        this.departureTime = departureTime;
        this.status = calculateStatus(order, departureTime);
        this.nextDepartureTime = departureTime.plusSeconds((long)Math.ceil(order.getDistance() * 2 * 60));
        previousDeliver.addAll(Arrays.asList(prevOrders));
    }

    public int getCurrentPathScore() {
        if (!previousDeliver.isEmpty()){
            return previousDeliver.get(0).getCurrentPathScore() + status;
        }
        return status;
    }

    public LocalTime getReturnTime(){
        return nextDepartureTime;
    }

    public void addPreviousDeliver(OrderFinal o){
        previousDeliver.add(o);
    }

    public static int calculateStatus(Order order, LocalTime departureTime) {
        LocalTime arrivalTime = departureTime.plusSeconds((long)Math.ceil(order.getDistance()*60));
        long diffInSeconds = ChronoUnit.SECONDS.between(order.getOrderTime(), arrivalTime);
        if (diffInSeconds <= 1 * 3600) { // in seconds
            return 1;
        }
        else if (diffInSeconds <= 3 * 3600) {
            return 0;
        }
        else {
            return -1;
        }
    }
}
