public class PremiumTicket extends Ticket{
    private static final double MULTIPLIER = 1.5;
    private static final double LOUNGE_FEE = 150.0;

    public PremiumTicket(String ticketId, String showId, String seatId, double basePrice) {
        super(ticketId, showId, seatId, basePrice);
    }
    @Override public double price()    { return getBasePrice() * MULTIPLIER + LOUNGE_FEE; }
    @Override public String category() { return "PREMIUM"; }
}
