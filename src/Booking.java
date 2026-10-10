import java.util.List;
import java.util.Objects;

public class Booking {
    private final String bookingId;
    private final String userId;
    private final List<Ticket> tickets;     // one booking, many ticket subtypes
    private BookingStatus status;
    private String paymentRef;

    public Booking(String bookingId, String userId, List<Ticket> tickets) {
        this.bookingId = Objects.requireNonNull(bookingId);
        this.userId    = Objects.requireNonNull(userId);
        if (tickets == null || tickets.isEmpty()) {
            throw new IllegalArgumentException("A booking needs at least one ticket");
        }
        this.tickets = List.copyOf(tickets);
        this.status  = BookingStatus.HELD;
    }

    /** Polymorphic aggregation — no instanceof checks needed. */
    public double total() {
        return Math.round(tickets.stream().mapToDouble(Ticket::priceWithTax).sum() * 100.0) / 100.0;
    }

    public String getBookingId()    { return bookingId; }
    public String getUserId()       { return userId; }
    public List<Ticket> getTickets(){ return tickets; }
    public BookingStatus getStatus(){ return status; }
    public String getPaymentRef()   { return paymentRef; }

    void markConfirmed(String paymentRef) {
        this.status = BookingStatus.CONFIRMED;
        this.paymentRef = paymentRef;
    }

    void markCancelled() {
        this.status = BookingStatus.CANCELLED;
    }

    @Override
    public String toString() {
        return "Booking{" + bookingId + ", user=" + userId
                + ", tickets=" + tickets.size() + ", total=\u20b9" + total()
                + ", status=" + status + "}";
    }
}
