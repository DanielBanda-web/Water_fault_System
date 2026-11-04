# Water Fault Reporting System

## Project Description
A web-based application for reporting water-related problems such as leaking pipes, burst pipes, and water shortages. Maintenance workers can be assigned to faults and administrators can update repair progress.

## Technologies
- Java 17
- Maven
- Javalin 6.6.0
- Gson
- HTML, CSS, and JavaScript
- File-based storage

## Features
- Submit and view fault reports
- Track fault status
- Assign maintenance workers
- Update repair progress
- Delete fault reports
- Save reports to `fault_reports.dat`

## Object-Oriented Programming Concepts
- Encapsulation: private fields and getters/setters
- Inheritance: `Customer` and `MaintenanceWorker` extend `User`
- Abstraction: `User` is an abstract class
- Polymorphism: `getRole()` has different implementations
- Interface: `MaintenanceWorker` implements `Repairable`
- Exception handling: `FaultNotFoundException`

## Run the project
1. Install Java 17 or later and Maven.
2. Open this folder in VS Code.
3. Open a terminal in this folder.
4. Run `mvn clean compile exec:java`.
5. Open http://localhost:7070 in your browser.

The `fault_reports.dat` file is created automatically when the first report is submitted.

### Group Members

| Full Name       | StudentID|
| --------------- | -------- |
| Daniel F Banda  | 2410386  |
| Ethel Kazembe   | 2410059  |
| Patience Chabala| 2420945  |
| Walen Mudenda   | 2410287  |
| Caroline Kaliba | 2420984  |
