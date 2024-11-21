package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import hotelmanager.demo.models.Role;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByIdAndHotelId(int employeeId, int hotelId);
    List<Employee> findByHotelId(int hotelId);

    @Query(value = """
        SELECT r.id, r.name FROM role r 
        JOIN user_role ur ON r.id = ur.role_id 
        JOIN user u ON ur.user_id = u.id 
        JOIN employee e ON e.user_id = u.id 
        WHERE e.id = :employeeId""", nativeQuery = true)
    List<Object[]> findRoleListByEmployeeId(@Param("employeeId") int employeeId);

    @Query(value = """
        SELECT u.username FROM employee e 
        JOIN user u ON e.user_id = u.id 
        WHERE e.id = :employeeId""", nativeQuery = true)
    String findUserNameEmployeeById(@Param("employeeId") int employeeId);

    @Query(value = """
        SELECT e.* FROM employee e 
        JOIN user u ON e.user_id = u.id 
        WHERE u.id = :userId""", nativeQuery = true)
    Employee findByUserId(@Param("userId") int userId);
}
