package hotelmanager.demo.services;

import hotelmanager.demo.dto.EmployeeDto;
import hotelmanager.demo.dto.EmployeeResponseDto;
import hotelmanager.demo.dto.auth.AuthDto;
import hotelmanager.demo.dto.auth.RoleDto;
import hotelmanager.demo.dto.auth.UserInfoDto;
import hotelmanager.demo.exceptions.NotFoundException;
import hotelmanager.demo.models.Employee;
import hotelmanager.demo.models.Role;
import hotelmanager.demo.models.enums.RoleType;
import hotelmanager.demo.repositories.EmployeeRepository;
import hotelmanager.demo.repositories.HotelRepository;
import hotelmanager.demo.repositories.UserRepository;
import hotelmanager.demo.security.CustomUserDetails;
import hotelmanager.demo.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hotelmanager.demo.models.Hotel;
import hotelmanager.demo.models.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

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
        //xóa role cũ trong database
        String username = employeeRepository.findUserNameEmployeeById(employeeId);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("profile not found"));
        userEntity.getRoles().clear();
        userRepository.save(userEntity);

        //cập nhật role mới
        if (employeeDto.getUser() != null && employeeDto.getUser().getRoles() != null) {
            List<Role> roles = new ArrayList<>();
            for (RoleDto roleDto: employeeDto.getUser().getRoles()) {
                Role role = new Role();
                role.setId(roleDto.getId());
                role.setName(roleDto.getName());
                roles.add(role);
            }
            userEntity.setRoles(roles);
            userRepository.save(userEntity);
        }

        modelMapper.map(employeeDto, employee);

        return employeeRepository.save(employee);
    }


    public EmployeeResponseDto getEmployeeById(int employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeResponseDto employeeDto = modelMapper.map(employee, EmployeeResponseDto.class);
        String username = employeeRepository.findUserNameEmployeeById(employeeId);
        
        // Convert Object[] to RoleDto
        List<Object[]> roleData = employeeRepository.findRoleListByEmployeeId(employeeId);
        List<Role> roles = roleData.stream()
            .map(data -> {
                Role role = new Role();
                role.setId((Integer) data[0]);
                role.setName(RoleType.valueOf((String) data[1]));
                return role;
            })
            .collect(Collectors.toList());

        UserInfoDto userDto = new UserInfoDto();
        userDto.setUsername(username);
        userDto.setRoles(roles);
        employeeDto.setUser(userDto);
        return employeeDto;
    }

    public EmployeeResponseDto getEmployeeByUserId(CustomUserDetails user) {
        Employee employee = employeeRepository.findByUserId(user.getUser().getId());
        if (employee == null) throw new NotFoundException("Employee not found");

        EmployeeResponseDto employeeDto = modelMapper.map(employee, EmployeeResponseDto.class);
        // Convert Object[] to RoleDto
        List<Object[]> roleData = employeeRepository.findRoleListByEmployeeId(employee.getId());
        List<Role> roles = roleData.stream()
            .map(data -> {
                Role role = new Role();
                role.setId((Integer) data[0]);
                role.setName(RoleType.valueOf((String) data[1]));
                return role;
            })
            .collect(Collectors.toList());

        UserInfoDto userDto = new UserInfoDto();
        userDto.setUsername(user.getUser().getUsername());
        userDto.setRoles(roles);
        employeeDto.setUser(userDto);
        return employeeDto;
    }

}
