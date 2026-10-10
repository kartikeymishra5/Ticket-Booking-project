import java.util.Objects;

public abstract class User {
    private final String id;
    private String name;
    private String email;

    protected User(String id, String name, String email) {
        this.id = Objects.requireNonNull(id, "id");
        setName(name);
        setEmail(email);
    }

    public abstract String role();

    public final String getId() { return id; }           // id is immutable

    public String getName() { return name; }

    public final void setName(String name) {             // encapsulated validation
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be blank");
        }
        this.name = name.trim();
    }

    public String getEmail() { return email; }

    public final void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
        this.email = email.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return role() + "[" + id + ", " + name + "]";
    }
}
