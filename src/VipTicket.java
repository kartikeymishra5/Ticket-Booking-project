public class VipTicket extends Ticket {
    private static final double MULTIPLIER = 2.5;
    private static final double SERVICE_FEE = 500.0;

    public VipTicket(String ticketId, String showId, String seatId, double basePrice) {
        super(ticketId, showId, seatId, basePrice);
    }
    @Override public double price()    { return getBasePrice() * MULTIPLIER + SERVICE_FEE; }
    @Override public String category() { return "VIP"; }
}
