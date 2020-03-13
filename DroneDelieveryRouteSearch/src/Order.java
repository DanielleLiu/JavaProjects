import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class Order {

    private String id;

    private LocalTime orderTime;

    private String location;

    private int x = 0;

    private int y = 0;

    private double distance = 0;

    public Order(String id, String location, String orderTime){
        this.id = id;
        this.orderTime = LocalTime.parse(orderTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.location = location;
        parseCoordinate(location);
        this.distance = (long) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    private void parseCoordinate(String location) {
        int beginningIndex = 1;
        char direction = location.charAt(0);
        for (int i = 1; i < location.length(); i++) {
            char s = location.charAt(i);
            if (Character.isAlphabetic(s)) {
                assignCoordinate(beginningIndex, i, direction);
                beginningIndex = i + 1;
                direction = s;
            }
            if (i == location.length() - 1) { // last digit
                assignCoordinate(beginningIndex, i+1, direction);
            }
        }
    }

    private void assignCoordinate(int beginningIndex, int endIndex, char direction){
        int length = Integer.valueOf(location.substring(beginningIndex, endIndex));
        if (direction == 'N'){ //the sign might not be necessary.
            this.y = length;
        } else if (direction == 'S'){
            this.y = -length;
        } else if (direction =='W'){
            this.x = -length;
        } else if (direction == 'E'){
            this.x = length;
        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the orderTime
     */
    public LocalTime getOrderTime() {
        return orderTime;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    @Override
    public String toString(){
//        return id+" "+orderTime +" "+location+" x:"+x+" y:"+y+" distance:"+distance;
        return id;
    }
}
