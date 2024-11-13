package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
    @Query(value = """
            SELECT DISTINCT *
            FROM customers c
            WHERE c.name LIKE %:query%
               OR c.phone_number LIKE %:query%
            """, nativeQuery = true)
    List<Customer> findAllCustomersByNameOrPhoneCus(@Param("query") String query);

}
