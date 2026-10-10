public interface PaymentMethod {
    String name();

    boolean pay(double amount);

    /** Reusable default behaviour shared by every implementation. */
    default String receipt(double amount) {
        return "Receipt: \u20b9" + amount + " via " + name();
    }
}
