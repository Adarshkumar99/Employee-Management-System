import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Main.java
 *
 * This is the entry point of the application. Its only job is to:
 *   1. Show a text menu to the user
 *   2. Read what the user typed
 *   3. Call the matching EmployeeDAO method
 *   4. Print the result back to the console
 *
 * Main.java deliberately contains NO SQL - all database logic lives in
 * EmployeeDAO.java. This separation makes the code much easier to
 * follow: if something is wrong with a query, look in EmployeeDAO; if
 * something is wrong with the menu/input, look here.
 */
public class Main {

    // Reused across the whole app instead of creating a new Scanner
    // every time we need input (creating multiple Scanners on System.in
    // can cause subtle bugs).
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmployeeDAO employeeDAO = new EmployeeDAO();

    public static void main(String[] args) {
        boolean running = true;

        // Simple menu loop: keep showing options until the user chooses
        // to exit (option 0).
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleAddEmployee();
                    break;
                case "2":
                    handleUpdateEmployee();
                    break;
                case "3":
                    handleDeleteEmployee();
                    break;
                case "4":
                    handleSearchEmployee();
                    break;
                case "5":
                    handleListEmployees();
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please try again.\n");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("===== Employee Management System =====");
        System.out.println("1. Add Employee");
        System.out.println("2. Update Employee");
        System.out.println("3. Delete Employee");
        System.out.println("4. Search Employee");
        System.out.println("5. List All Employees");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    /**
     * Collects new-employee details from the user, builds an Employee
     * object, and delegates the actual database INSERT to EmployeeDAO.
     */
    private static void handleAddEmployee() {
        System.out.println("\n-- Add Employee --");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Department: ");
        String department = scanner.nextLine();

        System.out.print("Salary: ");
        BigDecimal salary = new BigDecimal(scanner.nextLine());

        // Joining date defaults to today so the user doesn't need to
        // type a date manually every time. Could be extended to accept
        // custom input if needed.
        LocalDate joiningDate = LocalDate.now();

        Employee newEmployee = new Employee(name, email, department, salary, joiningDate);
        boolean success = employeeDAO.addEmployee(newEmployee);

        System.out.println(success ? "Employee added successfully!\n" : "Failed to add employee.\n");
    }

    /**
     * Asks for an employee id, then collects the new values and
     * delegates the UPDATE to EmployeeDAO.
     */
    private static void handleUpdateEmployee() {
        System.out.println("\n-- Update Employee --");

        System.out.print("Enter ID of employee to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("New Name: ");
        String name = scanner.nextLine();

        System.out.print("New Email: ");
        String email = scanner.nextLine();

        System.out.print("New Department: ");
        String department = scanner.nextLine();

        System.out.print("New Salary: ");
        BigDecimal salary = new BigDecimal(scanner.nextLine());

        // We keep the original joining date's format but let it be
        // re-entered here for simplicity; in a bigger app you might
        // fetch the existing employee first and only change selected fields.
        System.out.print("New Joining Date (YYYY-MM-DD): ");
        LocalDate joiningDate = LocalDate.parse(scanner.nextLine());

        Employee updatedEmployee = new Employee(id, name, email, department, salary, joiningDate);
        boolean success = employeeDAO.updateEmployee(updatedEmployee);

        System.out.println(success ? "Employee updated successfully!\n" : "No employee found with that ID.\n");
    }

    /**
     * Asks for an employee id and delegates the DELETE to EmployeeDAO.
     */
    private static void handleDeleteEmployee() {
        System.out.println("\n-- Delete Employee --");

        System.out.print("Enter ID of employee to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        boolean success = employeeDAO.deleteEmployee(id);

        System.out.println(success ? "Employee deleted successfully!\n" : "No employee found with that ID.\n");
    }

    /**
     * Asks for a keyword and prints every employee whose name matches.
     */
    private static void handleSearchEmployee() {
        System.out.println("\n-- Search Employee --");

        System.out.print("Enter name (or part of it) to search: ");
        String keyword = scanner.nextLine();

        List<Employee> results = employeeDAO.searchEmployee(keyword);

        if (results.isEmpty()) {
            System.out.println("No employees found matching \"" + keyword + "\".\n");
        } else {
            results.forEach(System.out::println);
            System.out.println();
        }
    }

    /**
     * Prints every employee currently stored in the database.
     */
    private static void handleListEmployees() {
        System.out.println("\n-- All Employees --");

        List<Employee> employees = employeeDAO.listEmployees();

        if (employees.isEmpty()) {
            System.out.println("No employees in the database yet.\n");
        } else {
            employees.forEach(System.out::println);
            System.out.println();
        }
    }
}
