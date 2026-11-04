package zm.waterfault.model;

import zm.waterfault.service.Repairable;

public class MaintenanceWorker extends User implements Repairable {
    public MaintenanceWorker(int id, String name, String phoneNumber) {
        super(id, name, phoneNumber);
    }
    @Override
    public String getRole() { return "Maintenance Worker"; }
    @Override
    public String repair() { return getName() + " is repairing the water fault."; }
}
