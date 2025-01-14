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
               AND c.hotel_id = :hotelId
               AND c.deleted = false
            """, nativeQuery = true)
    List<Customer> findAllCustomersByNameOrPhoneCus(@Param("query") String query, @Param("hotelId") Integer hotelId);

    List<Customer> findAllCustomersByHotelIdAndDeletedFalse(@Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT c.email
            FROM customers c
            LEFT JOIN bookings b ON b.customer_id = c.id
            WHERE b.id = :bookingId
            """, nativeQuery = true)
    String findEmailByBookingId(@Param("bookingId") Integer bookingId);
}
