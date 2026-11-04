package zm.waterfault.model;

public class Customer extends User {
    public Customer(int id, String name, String phoneNumber) {
        super(id, name, phoneNumber);
    }
    @Override
    public String getRole() { return "Customer"; }
}
