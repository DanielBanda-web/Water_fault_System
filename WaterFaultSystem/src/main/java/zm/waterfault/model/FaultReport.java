package zm.waterfault.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FaultReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id;
    private final String customerName;
    private final String phoneNumber;
    private final String faultType;
    private final String description;
    private final String location;
    private final LocalDateTime dateReported;
    private String status;
    private String assignedWorker;

    public FaultReport(int id, String customerName, String phoneNumber, String faultType,
                       String description, String location) {
        this.id = id;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.faultType = faultType;
        this.description = description;
        this.location = location;
        this.status = "OPEN";
        this.assignedWorker = "Not assigned";
        this.dateReported = LocalDateTime.now();
    }
    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getFaultType() { return faultType; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getAssignedWorker() { return assignedWorker; }
    public LocalDateTime getDateReported() { return dateReported; }
    public void setStatus(String status) { this.status = status; }
    public void setAssignedWorker(String assignedWorker) { this.assignedWorker = assignedWorker; }
}
