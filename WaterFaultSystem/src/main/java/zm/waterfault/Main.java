package zm.waterfault;

import io.javalin.Javalin;
import com.google.gson.Gson;

import zm.waterfault.service.ReportManager;
import zm.waterfault.model.FaultReport;
import zm.waterfault.model.Customer;
import zm.waterfault.model.MaintenanceWorker;
import zm.waterfault.model.User;
import zm.waterfault.exception.FaultNotFoundException;

import java.util.List;
import java.util.Map;

public class Main {

    private static final Gson gson = new Gson();
    private static final ReportManager manager = new ReportManager();

    public static void main(String[] args) {

        // Create Javalin application and serve frontend files
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        });

        // Handle fault not found errors
        app.exception(FaultNotFoundException.class, (e, ctx) -> {
            ctx.status(404);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("error", e.getMessage())));
        });

        // Handle invalid input
        app.exception(IllegalArgumentException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(Map.of("error", e.getMessage())));
        });

        // Handle unexpected errors
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(
                Map.of("error", "Internal server error.")
            ));
        });

        // Check whether the server is running
        app.get("/api/health", ctx -> {
            ctx.contentType("application/json");
            ctx.result(gson.toJson(
                Map.of("message",
                "Water Fault Reporting System is running.")
            ));
        });

        // Get all fault reports
        app.get("/api/reports", ctx -> {
            List<FaultReport> reports = manager.getAllReports();

            ctx.contentType("application/json");
            ctx.result(gson.toJson(reports));
        });

        // Get a specific fault report
        app.get("/api/reports/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            FaultReport report = manager.getReport(id);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(report));
        });

        // Submit a new fault report
        app.post("/api/reports", ctx -> {

            ReportRequest request =
                gson.fromJson(ctx.body(), ReportRequest.class);

            if (request == null) {
                throw new IllegalArgumentException(
                    "Request body is required."
                );
            }

            FaultReport report = manager.createReport(
                request.customerName,
                request.phoneNumber,
                request.faultType,
                request.description,
                request.location
            );

            ctx.status(201);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(report));
        });

        // Update fault report status
        app.put("/api/reports/{id}/status", ctx -> {

            int id = Integer.parseInt(ctx.pathParam("id"));

            StatusRequest request =
                gson.fromJson(ctx.body(), StatusRequest.class);

            if (request == null || request.status == null
                    || request.status.isBlank()) {
                throw new IllegalArgumentException(
                    "Status is required."
                );
            }

            FaultReport report =
                manager.updateStatus(id, request.status);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(report));
        });

        // Assign maintenance worker
        app.patch("/api/reports/{id}/assign", ctx -> {

            int id = Integer.parseInt(ctx.pathParam("id"));

            WorkerRequest request =
                gson.fromJson(ctx.body(), WorkerRequest.class);

            if (request == null || request.workerName == null
                    || request.workerName.isBlank()) {
                throw new IllegalArgumentException(
                    "Worker name is required."
                );
            }

            FaultReport report =
                manager.assignWorker(id, request.workerName);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(report));
        });

        // Delete a fault report
        app.delete("/api/reports/{id}", ctx -> {

            int id = Integer.parseInt(ctx.pathParam("id"));

            manager.deleteReport(id);

            ctx.status(204);
        });

        // Demonstrate OOP polymorphism
        app.get("/api/demo-polymorphism", ctx -> {

            List<User> users = List.of(
                new Customer(1, "John", "0977000000"),
                new MaintenanceWorker(2, "Peter", "0966000000")
            );

            List<Map<String, String>> results = users.stream()
                .map(user -> Map.of(
                    "name", user.getName(),
                    "role", user.getRole()
                ))
                .toList();

            MaintenanceWorker worker =
                new MaintenanceWorker(2, "Peter", "0966000000");

            String repairMessage = manager.performRepair(worker);

            ctx.contentType("application/json");
            ctx.result(gson.toJson(
                Map.of(
                    "users", results,
                    "repairMessage", repairMessage
                )
            ));
        });

        app.start(7070);

        System.out.println(
            "Water Fault Reporting System running at http://localhost:7070"
        );
    }

    // Request classes

    static class ReportRequest {
        String customerName;
        String phoneNumber;
        String faultType;
        String description;
        String location;
    }

    static class StatusRequest {
        String status;
    }

    static class WorkerRequest {
        String workerName;
    }
}