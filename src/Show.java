import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Show {
    private final String id;
    private final String title;
    private final Map<String, Seat> seats;

    public Show(String id, String title, int rows, int seatsPerRow, double basePrice) {
        if (rows <= 0 || seatsPerRow <= 0) {
            throw new IllegalArgumentException("rows and seatsPerRow must be > 0");
        }
        this.id = id;
        this.title = title;

        Map<String, Seat> map = new LinkedHashMap<>();
        for (int r = 0; r < rows; r++) {
            char row = (char) ('A' + r);
            for (int c = 1; c <= seatsPerRow; c++) {
                String seatId = "" + row + c;
                map.put(seatId, new Seat(seatId, basePrice));
            }
        }
        this.seats = Collections.unmodifiableMap(map);   // encapsulated view
    }

    public String getId()    { return id; }
    public String getTitle() { return title; }
    public Map<String, Seat> getSeats() { return seats; }
    public Seat seat(String seatId)     { return seats.get(seatId); }
}
