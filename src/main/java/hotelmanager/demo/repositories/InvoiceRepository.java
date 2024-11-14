package hotelmanager.demo.repositories;

import hotelmanager.demo.dto.invoiceDtos.IInvoiceDto;
import hotelmanager.demo.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query(value = """
            SELECT i.id AS id,
                   i.total_amount AS totalAmount,
                   i.payment_method AS paymentMethod, 
                   i.booking_id AS bookingId,
                   i.customer_id AS customerId,
                   i.issue_date AS issueDate,
                   i.payment_status AS paymentStatus
            FROM invoice i
            WHERE i.hotel_id = :hotelId AND i.deleted = false
            ORDER BY i.issue_date DESC
            """, nativeQuery = true)
    List<IInvoiceDto> findAllInvoiceDtos(@Param("hotelId") Integer hotelId);

    @Query(value = """
            SELECT i.id AS id,
                   i.total_amount AS totalAmount,
                   i.payment_method AS paymentMethod,
                   i.booking_id AS bookingId, 
                   i.customer_id AS customerId,
                   i.issue_date AS issueDate,
                   i.payment_status AS paymentStatus
            FROM invoice i
            WHERE i.id = :invoiceId AND i.deleted = false
            """, nativeQuery = true)
    IInvoiceDto findInvoiceDtoById(@Param("invoiceId") Integer invoiceId);

    @Query(value = """
            SELECT i.id AS id,
                   i.total_amount AS totalAmount,
                   i.payment_method AS paymentMethod,
                   i.booking_id AS bookingId,
                   i.customer_id AS customerId,
                   i.issue_date AS issueDate,
                   i.payment_status AS paymentStatus
            FROM invoice i
            WHERE i.customer_id = :customerId AND i.deleted = false
            ORDER BY i.issue_date DESC
            """, nativeQuery = true)
    List<IInvoiceDto> findInvoiceDtosByCustomerId(@Param("customerId") Integer customerId);

    @Query(value = """
            SELECT i.id AS id,
                   i.total_amount AS totalAmount,
                   i.payment_method AS paymentMethod,
                   i.booking_id AS bookingId,
                   i.customer_id AS customerId,
                   i.issue_date AS issueDate,
                   i.payment_status AS paymentStatus
            FROM invoice i
            WHERE i.booking_id = :bookingId AND i.deleted = false
            """, nativeQuery = true)
    IInvoiceDto findInvoiceDtoByBookingId(@Param("bookingId") Integer bookingId);

    @Query(value = """
            SELECT COUNT(i.booking_id)
            FROM invoice i
            WHERE i.booking_id = :bookingId AND i.deleted = false
            """, nativeQuery = true)
    Integer countByBookingId(@Param("bookingId") Integer bookingId);

}
