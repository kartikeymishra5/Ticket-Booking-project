public class RegularTicket  extends Ticket {
    public RegularTicket (String ticketId, String showId, String seatId, double basePrice) {
        super(ticketId, showId, seatId, basePrice);
    }
    @Override public double price()     { return getBasePrice(); }
    @Override public String category()  { return "REGULAR"; }
}
