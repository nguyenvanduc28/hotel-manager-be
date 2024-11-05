package hotelmanager.demo.services.bookings;

import hotelmanager.demo.dto.bookingDtos.CustomerDto;
import hotelmanager.demo.models.Customer;
import hotelmanager.demo.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setGender(customerDto.getGender());
        customer.setBirthDay(customerDto.getBirthDay());
        customer.setNationality(customerDto.getNationality());
        customer.setIdentityNumber(customerDto.getIdentityNumber());
        customer.setAddress(customerDto.getAddress());
        customer.setNotes(customerDto.getNotes());

        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();

        for (Customer customer : customers) {
            CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
            customerDtos.add(customerDto);
        }

        return customerDtos;
    }
    @Transactional(readOnly = true)
    public List<CustomerDto> searchCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllCustomers();
        }
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return List.of(modelMapper.map(customers, CustomerDto[].class));
    }
}
