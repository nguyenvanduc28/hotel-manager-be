package hotelmanager.demo.repositories;

import hotelmanager.demo.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("select u from UserEntity u where u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    Boolean existsByUsername(String username);

//    @Query(value = "select u from UserEntity u where (u.fullName like %:search% or u.phone like %:search%)" +
//            " and not exists (" +
//            " select r" +
//            " from u.roles r" +
//            " where r.name = 'ADMIN'" +
//            ")" +
//            " order by u.updatedAt desc")
//    Page<UserEntity> getAllStaffs(@Param("search") String search, Pageable paging);
}
