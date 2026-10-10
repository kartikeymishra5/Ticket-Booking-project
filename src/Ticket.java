import java.util.Objects;

public abstract class Ticket {
    private final String ticketId;
    private final String showId;
    private final String seatId;
    private final double basePrice;

    protected Ticket(String ticketId, String showId, String seatId, double basePrice) {
        this.ticketId  = Objects.requireNonNull(ticketId);
        this.showId    = Objects.requireNonNull(showId);
        this.seatId    = Objects.requireNonNull(seatId);
        this.basePrice = basePrice;
    }

    /** Template method — concrete subclasses supply the pricing rule. */
    public final double priceWithTax() {
        return Math.round((price() * 1.18) * 100.0) / 100.0;   // 18% GST
    }

    public abstract double price();        // runtime polymorphism
    public abstract String category();     // runtime polymorphism

    public String getTicketId() { return ticketId; }
    public String getShowId()   { return showId; }
    public String getSeatId()   { return seatId; }
    public double getBasePrice(){ return basePrice; }

    @Override
    public String toString() {
        return category() + "-Ticket[" + ticketId + ", seat " + seatId
                + ", \u20b9" + priceWithTax() + "]";
    }
}
