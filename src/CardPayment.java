public class CardPayment implements PaymentMethod{
    private final String maskedCard;

    public CardPayment(String maskedCard) { this.maskedCard = maskedCard; }

    @Override public String name() { return "CARD " + maskedCard; }

    @Override public boolean pay(double amount) {
        // Real integration would call a gateway here.
        return amount > 0;
    }
}
