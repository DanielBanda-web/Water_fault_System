package zm.waterfault.model;

public abstract class User {
    private final int id;
    private final String name;
    private final String phoneNumber;

    public User(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public abstract String getRole();
}
