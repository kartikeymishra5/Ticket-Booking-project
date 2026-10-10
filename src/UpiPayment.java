public class UpiPayment implements PaymentMethod{
    private final String vpa;

    public UpiPayment(String vpa) { this.vpa = vpa; }

    @Override public String name() { return "UPI " + vpa; }

    @Override public boolean pay(double amount) { return amount > 0; }
}
