import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeDAO.java  (DAO = Data Access Object)
 *
 * This class is the ONLY place in the whole project that talks SQL to
 * the database. Every method here does exactly one CRUD operation:
 *
 *   addEmployee()    -> SQL INSERT
 *   updateEmployee() -> SQL UPDATE
 *   deleteEmployee() -> SQL DELETE
 *   searchEmployee() -> SQL SELECT ... WHERE
 *   listEmployees()  -> SQL SELECT * (all rows)
 *
 * Why isolate SQL in a DAO instead of writing queries inside Main.java?
 *  - Separation of concerns: Main.java only deals with user
 *    interaction (menus, input), EmployeeDAO only deals with the
 *    database. Each class has ONE job, which makes the code easier to
 *    read, test, and maintain.
 *  - If we ever switch database (e.g. PostgreSQL -> MySQL), we only
 *    need to touch this file, not the whole application.
 *
 * We use PreparedStatement everywhere instead of building raw SQL
 * strings with string-concatenation. PreparedStatement automatically
 * escapes user input, which prevents SQL Injection attacks - this is
 * a critical security practice, not just a style choice.
 */
public class EmployeeDAO {

    /**
     * Inserts a new employee row into the database.
     *
     * @param employee the Employee object to save (id is ignored -
     *                 the database generates it automatically)
     * @return true if exactly one row was inserted, false otherwise
     */
    public boolean addEmployee(Employee employee) {
        // "?" placeholders are filled in safely below - never concatenate
        // user input directly into the SQL string.
        String sql = "INSERT INTO employees (name, email, department, salary, joining_date) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getDepartment());
            stmt.setBigDecimal(4, employee.getSalary());
            stmt.setObject(5, employee.getJoiningDate());

            int rowsInserted = stmt.executeUpdate(); // executeUpdate() is used for
                                                       // INSERT/UPDATE/DELETE - it
                                                       // returns the number of rows affected
            return rowsInserted == 1;

        } catch (SQLException e) {
            System.out.println("Error while adding employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates every editable field of an existing employee, identified
     * by their id.
     *
     * @param employee an Employee object whose id must already exist
     *                 in the database, and whose other fields hold the
     *                 NEW values to save
     * @return true if a row with that id was found and updated
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees " +
                     "SET name = ?, email = ?, department = ?, salary = ?, joining_date = ? " +
                     "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getDepartment());
            stmt.setBigDecimal(4, employee.getSalary());
            stmt.setObject(5, employee.getJoiningDate());
            stmt.setInt(6, employee.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated == 1;

        } catch (SQLException e) {
            System.out.println("Error while updating employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an employee by id.
     *
     * @param id the primary key of the employee to remove
     * @return true if a row was found and deleted
     */
    public boolean deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted == 1;

        } catch (SQLException e) {
            System.out.println("Error while deleting employee: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches employees whose name contains the given keyword
     * (case-insensitive, partial match - e.g. searching "ra" would
     * match "Rahul" and "Priya").
     *
     * @param keyword text to search for inside the employee's name
     * @return a list of matching employees (empty list if none found)
     */
    public List<Employee> searchEmployee(String keyword) {
        List<Employee> results = new ArrayList<>();

        // ILIKE = case-insensitive LIKE (PostgreSQL specific).
        // Wrapping the keyword with '%' turns it into a "contains" search.
        String sql = "SELECT * FROM employees WHERE name ILIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = stmt.executeQuery()) { // executeQuery() is used for
                                                         // SELECT statements - it
                                                         // returns a ResultSet (rows)
                while (rs.next()) {
                    results.add(mapRowToEmployee(rs));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while searching employee: " + e.getMessage());
        }

        return results;
    }

    /**
     * Fetches every employee row from the database, ordered by id.
     *
     * @return a list of all employees (empty list if the table is empty)
     */
    public List<Employee> listEmployees() {
        List<Employee> results = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(mapRowToEmployee(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error while listing employees: " + e.getMessage());
        }

        return results;
    }

    /**
     * Helper method: converts one row of a ResultSet into an Employee
     * object. Pulled out into its own method because searchEmployee()
     * and listEmployees() both need this exact same row-to-object
     * mapping - avoids duplicating the same code twice (DRY principle:
     * "Don't Repeat Yourself").
     */
    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("department"),
                rs.getBigDecimal("salary"),
                rs.getDate("joining_date").toLocalDate()
        );
    }
}
