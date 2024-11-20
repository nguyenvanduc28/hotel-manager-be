package hotelmanager.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;
import hotelmanager.demo.dto.ResponseObject;
import hotelmanager.demo.models.Employee;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.EmployeeService;
import jakarta.validation.Valid;
import hotelmanager.demo.dto.EmployeeDto;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("getall")
    public ResponseEntity<ResponseObject> getAllEmployees(@AuthenticationPrincipal CustomUserDetails user) {
        List<Employee> employees = employeeService.getAllEmployees(user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(employees)
                .message("Fetched all employees")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseObject> getEmployeeById(@PathVariable Integer id) {
        EmployeeDto employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(employee)
                .message("Fetched employee by id")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("create")
    public ResponseEntity<ResponseObject> createEmployee(
            @RequestBody @Valid EmployeeDto employeeDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Employee employee = employeeService.createEmployee(employeeDto, user.getUser().getHotelId());
        
        return ResponseEntity.ok(ResponseObject.builder()
                .data(employee)
                .message("Employee created successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("update/{id}")
    public ResponseEntity<ResponseObject> updateEmployee(
            @RequestBody @Valid EmployeeDto employeeDto,
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {

        Employee updatedEmployee = employeeService.updateEmployee(employeeDto, id, user.getUser().getHotelId());
        return ResponseEntity.ok(ResponseObject.builder()
                .data(updatedEmployee)
                .message("Employee updated successfully") 
                .responseCode(HttpStatus.OK.value())
                .build());
    }
}
