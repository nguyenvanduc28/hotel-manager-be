package hotelmanager.demo.repositories;

import hotelmanager.demo.models.Role;
import hotelmanager.demo.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select r from Role r where r.name = :name")
    Role findByName(@Param("name") String name);
}

