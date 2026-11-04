package zm.waterfault.service;

import zm.waterfault.model.FaultReport;
import zm.waterfault.model.MaintenanceWorker;
import zm.waterfault.exception.FaultNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportManager {
    private final List<FaultReport> reports;
    private final Map<Integer, FaultReport> reportMap;
    private final FileStorage storage;
    private int nextId;

    public ReportManager() {
        storage = new FileStorage();
        reports = storage.loadReports();
        reportMap = new HashMap<>();
        nextId = 1;
        for (FaultReport report : reports) {
            reportMap.put(report.getId(), report);
            nextId = Math.max(nextId, report.getId() + 1);
        }
    }

    public FaultReport createReport(String name, String phone, String type,
                                    String description, String location) {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()
                || type == null || type.isBlank() || description == null || description.isBlank()
                || location == null || location.isBlank()) {
            throw new IllegalArgumentException("All fields are required.");
        }
        FaultReport report = new FaultReport(nextId++, name.trim(), phone.trim(),
                type.trim(), description.trim(), location.trim());
        reports.add(report);
        reportMap.put(report.getId(), report);
        storage.saveReports(reports);
        return report;
    }

    public List<FaultReport> getAllReports() { return new ArrayList<>(reports); }

    public FaultReport getReport(int id) {
        FaultReport report = reportMap.get(id);
        if (report == null) throw new FaultNotFoundException(id);
        return report;
    }

    public FaultReport updateStatus(int id, String status) {
        if (status == null) throw new IllegalArgumentException("Status is required.");
        String updatedStatus = status.trim().toUpperCase();
        if (!List.of("OPEN", "ASSIGNED", "IN_PROGRESS", "RESOLVED").contains(updatedStatus)) {
            throw new IllegalArgumentException("Invalid fault status.");
        }
        FaultReport report = getReport(id);
        report.setStatus(updatedStatus);
        storage.saveReports(reports);
        return report;
    }

    public FaultReport assignWorker(int id, String worker) {
        if (worker == null || worker.isBlank()) {
            throw new IllegalArgumentException("Worker name is required.");
        }
        FaultReport report = getReport(id);
        report.setAssignedWorker(worker.trim());
        if ("OPEN".equals(report.getStatus())) report.setStatus("ASSIGNED");
        storage.saveReports(reports);
        return report;
    }

    public String performRepair(MaintenanceWorker worker) {
        Repairable repairable = worker;
        return repairable.repair();
    }

    public void deleteReport(int id) {
        FaultReport report = getReport(id);
        reports.remove(report);
        reportMap.remove(id);
        storage.saveReports(reports);
    }
}
