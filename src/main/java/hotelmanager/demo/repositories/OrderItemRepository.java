package hotelmanager.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hotelmanager.demo.models.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrderId(Integer orderId);

    @Modifying
    @Query(value = "DELETE FROM order_item WHERE order_id = :orderId", nativeQuery = true)
    void deleteByOrderId(@Param("orderId") Integer orderId);
} 