package repository;


import model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/*public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId, Pageable pageable);
}
*/


public interface TodoRepository extends JpaRepository<Todo, Long> {
    Page<Todo> findByUserId(Long userId, Pageable pageable);
}

//enable search
/*public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId AND (t.title LIKE %:keyword% OR t.description LIKE %:keyword%)")
    Page<Todo> searchByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}*/

