import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.DoubleUnaryOperator;

public class BookingManager {
    private final Map<String, Show> shows = new ConcurrentHashMap<>();
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1000);

    public void addShow(Show show) { shows.put(show.getId(), show); }

    /** Factory method: picks the concrete Ticket subtype from the requested category. */
    public Booking book(String userId, String showId, List<String> seatIds,
                        String category, PaymentMethod payment,
                        DoubleUnaryOperator discount) {

        Show show = requireShow(showId);
        if (seatIds == null || seatIds.isEmpty()) {
            throw new BookingException("No seats selected");
        }

        synchronized (show) {
            List<Seat> reserved = new ArrayList<>();
            for (String raw : seatIds) {
                String seatId = raw.trim().toUpperCase();
                Seat seat = show.seat(seatId);
                if (seat == null) {
                    throw new BookingException("Unknown seat " + seatId);
                }
                if (seat.getStatus() != SeatStatus.AVAILABLE) {
                    throw new BookingException("Seat " + seatId + " is not available");
                }
                seat.setStatus(SeatStatus.HELD);
                reserved.add(seat);
            }

            List<Ticket> tickets = new ArrayList<>();
            for (Seat seat : reserved) {
                String ticketId = "TKT-" + sequence.incrementAndGet();
                tickets.add(createTicket(category, ticketId, showId, seat));
            }

            Booking booking = new Booking("BKG-" + sequence.incrementAndGet(), userId, tickets);

            double payable = discount == null ? booking.total() : discount.applyAsDouble(booking.total());
            if (!payment.pay(payable)) {
                reserved.forEach(s -> s.setStatus(SeatStatus.AVAILABLE));   // rollback
                throw new BookingException("Payment declined via " + payment.name());
            }

            reserved.forEach(s -> s.setStatus(SeatStatus.BOOKED));
            booking.markConfirmed(payment.receipt(payable));
            bookings.put(booking.getBookingId(), booking);
            return booking;
        }
    }

    /** Single switch-free factory that maps a category name to a Ticket subclass. */
    private Ticket createTicket(String category, String ticketId, String showId, Seat seat) {
        String type = category == null ? "REGULAR" : category.trim().toUpperCase();
        return switch (type) {
            case "REGULAR" -> new RegularTicket(ticketId, showId, seat.getId(), seat.getBasePrice());
            case "PREMIUM" -> new PremiumTicket(ticketId, showId, seat.getId(), seat.getBasePrice());
            case "VIP"     -> new VipTicket(ticketId, showId, seat.getId(), seat.getBasePrice());
            default        -> throw new BookingException("Unknown ticket category: " + category);
        };
    }

    public Booking cancel(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) throw new BookingException("Unknown booking: " + bookingId);

        Show show = requireShow(showOf(booking));

        synchronized (show) {
            if (booking.getStatus() == BookingStatus.CANCELLED) return booking;
            for (Ticket t : booking.getTickets()) {
                Seat seat = show.seat(t.getSeatId());
                if (seat != null) seat.setStatus(SeatStatus.AVAILABLE);
            }
            booking.markCancelled();
            return booking;
        }
    }

    public int availableSeats(String showId) {
        Show show = requireShow(showId);
        synchronized (show) {
            int count = 0;
            for (Seat seat : show.getSeats().values()) {
                if (seat.getStatus() == SeatStatus.AVAILABLE) count++;
            }
            return count;
        }
    }

    private String showOf(Booking booking) {
        return booking.getTickets().get(0).getShowId();
    }

    private Show requireShow(String showId) {
        Show show = shows.get(showId);
        if (show == null) throw new BookingException("Unknown show: " + showId);
        return show;
    }
}
