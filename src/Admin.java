public class Admin extends User{
    public Admin(String id, String name, String email) {
        super(id, name, email);
    }

    @Override public String role() { return "ADMIN"; }

    public Show createShow(String showId, String title, int rows, int seatsPerRow, double basePrice) {
        return new Show(showId, title, rows, seatsPerRow, basePrice);
    }
}
