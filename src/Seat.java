public class Seat {
    private final String id;
    private final double basePrice;
    private SeatStatus status = SeatStatus.AVAILABLE;

    public Seat(String id, double basePrice) {
        if (basePrice <= 0) throw new IllegalArgumentException("basePrice must be > 0");
        this.id = id;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + ":" + status;
    }
}
