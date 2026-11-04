const API_URL = "http://localhost:7070/api";

document.addEventListener("DOMContentLoaded", () => {
    const reportForm = document.getElementById("reportForm");
    const reportsContainer = document.getElementById("reportsContainer");
    const refreshButton = document.getElementById("refreshReports");
    const message = document.getElementById("message");

    async function apiRequest(endpoint, options = {}) {
        const response = await fetch(`${API_URL}${endpoint}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...options.headers
            }
        });

        if (response.status === 204) {
            return null;
        }

        const text = await response.text();

        let data;

        try {
            data = text ? JSON.parse(text) : null;
        } catch {
            throw new Error("The server returned an invalid response.");
        }

        if (!response.ok) {
            throw new Error(data?.error || "Something went wrong.");
        }

        return data;
    }

    function showMessage(text, type = "success") {
        if (!message) return;

        message.textContent = text;
        message.className = type;
    }

    async function loadReports() {
        if (!reportsContainer) return;

        reportsContainer.textContent = "Loading reports...";

        try {
            const reports = await apiRequest("/reports");

            reportsContainer.replaceChildren();

            if (!Array.isArray(reports) || reports.length === 0) {
                reportsContainer.textContent = "No water fault reports found.";
                return;
            }

            reports.forEach(report => {
                const card = document.createElement("div");
                card.className = "report-card";

                const title = document.createElement("h3");
                title.textContent = `Report #${report.id}`;

                const details = [
                    ["Customer", report.customerName],
                    ["Phone", report.phoneNumber],
                    ["Fault Type", report.faultType],
                    ["Description", report.description],
                    ["Location", report.location],
                    ["Status", report.status],
                    ["Assigned Worker", report.workerName || report.assignedWorker || "Not assigned"]
                ];

                card.appendChild(title);

                details.forEach(([label, value]) => {
                    const paragraph = document.createElement("p");
                    paragraph.textContent = `${label}: ${value ?? "Not available"}`;
                    card.appendChild(paragraph);
                });

                const statusSelect = document.createElement("select");

                ["Pending", "In Progress", "Resolved"].forEach(status => {
                    const option = document.createElement("option");
                    option.value = status;
                    option.textContent = status;
                    statusSelect.appendChild(option);
                });

                statusSelect.value = details.find(item => item[0] === "Status")?.[1] || "Pending";

                const updateButton = document.createElement("button");
                updateButton.textContent = "Update Status";

                updateButton.addEventListener("click", async () => {
                    try {
                        await apiRequest(`/reports/${report.id}/status`, {
                            method: "PUT",
                            body: JSON.stringify({
                                status: statusSelect.value
                            })
                        });

                        showMessage("Report status updated successfully.");
                        await loadReports();
                    } catch (error) {
                        showMessage(error.message, "error");
                    }
                });

                const workerInput = document.createElement("input");
                workerInput.type = "text";
                workerInput.placeholder = "Maintenance worker name";

                const assignButton = document.createElement("button");
                assignButton.textContent = "Assign Worker";

                assignButton.addEventListener("click", async () => {
                    if (!workerInput.value.trim()) {
                        showMessage("Enter a worker name.", "error");
                        return;
                    }

                    try {
                        await apiRequest(`/reports/${report.id}/assign`, {
                            method: "PATCH",
                            body: JSON.stringify({
                                workerName: workerInput.value.trim()
                            })
                        });

                        showMessage("Maintenance worker assigned successfully.");
                        await loadReports();
                    } catch (error) {
                        showMessage(error.message, "error");
                    }
                });

                const deleteButton = document.createElement("button");
                deleteButton.textContent = "Delete Report";

                deleteButton.addEventListener("click", async () => {
                    try {
                        await apiRequest(`/reports/${report.id}`, {
                            method: "DELETE"
                        });

                        showMessage("Report deleted successfully.");
                        await loadReports();
                    } catch (error) {
                        showMessage(error.message, "error");
                    }
                });

                card.append(
                    statusSelect,
                    updateButton,
                    workerInput,
                    assignButton,
                    deleteButton
                );

                reportsContainer.appendChild(card);
            });

        } catch (error) {
            reportsContainer.textContent = "Could not load reports.";
            showMessage(error.message, "error");
        }
    }

    if (reportForm) {
        reportForm.addEventListener("submit", async event => {
            event.preventDefault();

            const report = {
                customerName: document.getElementById("customerName")?.value.trim(),
                phoneNumber: document.getElementById("phoneNumber")?.value.trim(),
                faultType: document.getElementById("faultType")?.value,
                description: document.getElementById("description")?.value.trim(),
                location: document.getElementById("location")?.value.trim()
            };

            if (Object.values(report).some(value => !value)) {
                showMessage("Please complete all report fields.", "error");
                return;
            }

            try {
                await apiRequest("/reports", {
                    method: "POST",
                    body: JSON.stringify(report)
                });

                showMessage("Water fault report submitted successfully.");
                reportForm.reset();
                await loadReports();

            } catch (error) {
                showMessage(error.message, "error");
            }
        });
    }

    if (refreshButton) {
        refreshButton.addEventListener("click", loadReports);
    }

    loadReports();
});