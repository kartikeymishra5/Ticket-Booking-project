public class Customer extends   User{

    private double walletBalance;

    public Customer(String id, String name, String email) {
        super(id, name, email);
    }

    @Override public String role() { return "CUSTOMER"; }

    public double getWalletBalance() { return walletBalance; }

    public void credit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Credit must be positive");
        this.walletBalance += amount;
    }

    public void debit(double amount) {
        if (amount > walletBalance) {
            throw new IllegalStateException("Insufficient wallet balance");
        }
        this.walletBalance -= amount;
    }
}
