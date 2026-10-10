import java.util.List;

public class Testmain {

    public static void main(String[] args) {

        BookingManager manager = new BookingManager();
        Admin admin = new Admin("U-01", "Ritik", "admin@ashna.ai");
        Customer alice = new Customer("U-02", "Alice", "alice@example.com");
        Customer bob   = new Customer("U-03", "Bob", "bob@example.com");

        Show show = admin.createShow("SHW-1", "Interstellar", 4, 6, 200.0);
        manager.addShow(show);
        System.out.println("Available after setup: " + manager.availableSeats("SHW-1"));

// Polymorphism #1: same book() call, three different Ticket subtypes
        Booking b1 = manager.book(alice.getId(), "SHW-1", List.of("A1", "A2"),
                "PREMIUM", new CardPayment("****4242"), null);
        System.out.println(b1);

        Booking b2 = manager.book(bob.getId(), "SHW-1", List.of("B1"),
                "VIP", new UpiPayment("bob@upi"), null);
        System.out.println(b2);

// Polymorphism #2: a Discount lambda is injected at call time
        Booking b3 = manager.book(alice.getId(), "SHW-1", List.of("C1"),
                "REGULAR", new UpiPayment("alice@upi"), total -> total * 0.9);
        System.out.println(b3);

// Polymorphism #3: role() differs per User subclass
        System.out.println(admin);
        System.out.println(alice);

// Failure + rollback: B1 already booked
        try {
            manager.book(bob.getId(), "SHW-1", List.of("B1"), "REGULAR",
                    new CardPayment("****1111"), null);
        } catch (RuntimeException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }

        manager.cancel(b2.getBookingId());
        System.out.println("Available after cancel: " + manager.availableSeats("SHW-1"));
    }
}
