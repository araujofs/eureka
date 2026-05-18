package br.edu.ifpb.pweb2.eureka.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByName(String name);

  Optional<User> findByName(String name);

  @Query("select u from User u left join fetch u.results re left join fetch re.answers a left join a.question where u.id = :userId")
  Optional<User> findByIdWithResults(@Param("userId") Long userId);

  @EntityGraph(attributePaths = {"results", "results.answers", "results.answers.question"})
  List<User> findAllByAdminFalse();
}
