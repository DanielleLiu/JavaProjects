import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 *
 */
public class Machine {

    private LocalTime start = LocalTime.of(6, 0,0);
    private LocalTime end = LocalTime.of(22,0,0);
    private LocalTime current = LocalTime.of(6, 0,0);
    private static Machine m;

    private Machine(){
    }

    public static Machine getMachine(){
        if (m == null){
            m = new Machine();
        }
        return m;
    }

    public void setCurrentTime(String time){
        this.current = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public LocalTime getCurrentTime() {
        return current;
    }

}
