package hotelmanager.demo.services;

import hotelmanager.demo.dto.EmployeeDto;
import hotelmanager.demo.dto.auth.AuthDto;
import hotelmanager.demo.dto.auth.RoleDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Employee;
import hotelmanager.demo.models.Role;
import hotelmanager.demo.repositories.EmployeeRepository;
import hotelmanager.demo.repositories.HotelRepository;
import hotelmanager.demo.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.models.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AuthService authService;


    private ModelMapper modelMapper = new ModelMapper();

    public List<Employee> getAllEmployees(int hotelId) {
        return employeeRepository.findByHotelId(hotelId);
    }

    @Transactional
    public Employee createEmployee(EmployeeDto employeeDto, int hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));

        UserEntity user = authService.createUser(employeeDto.getUser(), hotelId);

        if (user == null) {
            throw new RuntimeException("User registration failed");
        }

        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setGender(employeeDto.getGender());
        employee.setBirthDay(employeeDto.getBirthDay());
        employee.setNationality(employeeDto.getNationality());
        employee.setIdentityNumber(employeeDto.getIdentityNumber());
        employee.setAddress(employeeDto.getAddress());
        employee.setNotes(employeeDto.getNotes());
        employee.setStartDate(employeeDto.getStartDate());
        employee.setStatus(employeeDto.getStatus());
        employee.setProfilePictureUrl(employeeDto.getProfilePictureUrl());
        employee.setEmergencyContactName(employeeDto.getEmergencyContactName());
        employee.setEmergencyContactRelationship(employeeDto.getEmergencyContactRelationship());
        employee.setEmergencyContactPhone(employeeDto.getEmergencyContactPhone());
        employee.setPositionName(employeeDto.getPositionName());
        employee.setUserId(user.getId());
        employee.setHotelId(hotelId);

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(EmployeeDto employeeDto, int employeeId, int hotelId) {
        Employee employee = employeeRepository.findByIdAndHotelId(employeeId, hotelId);
        if (employee == null) throw new NotFoundException("Employee not found");

        modelMapper.map(employeeDto, employee);

        return employeeRepository.save(employee);
    }


    public EmployeeDto getEmployeeById(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        String username = employeeRepository.findUserNameEmployeeById(employeeId);
        List<RoleDto> roles = List.of(modelMapper.map(employeeRepository.findRoleListByEmployeeId(employeeId), RoleDto[].class));

        AuthDto userDto = new AuthDto(username, "", roles);
        employeeDto.setUser(userDto);
        return employeeDto;
    }   

    public List<Role> getRolesEmployee(int employeeId) {
        return employeeRepository.findRoleListByEmployeeId(employeeId);
    }

}
