package zm.waterfault.service;

import zm.waterfault.model.FaultReport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final String fileName = "fault_reports.dat";

    public void saveReports(List<FaultReport> reports) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
            output.writeObject(new ArrayList<>(reports));
        } catch (IOException e) {
            throw new RuntimeException("Could not save reports.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<FaultReport> loadReports() {
        File file = new File(fileName);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (List<FaultReport>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Could not load reports.", e);
        }
    }
}
