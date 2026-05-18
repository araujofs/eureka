package br.edu.ifpb.pweb2.eureka.race;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RaceRepository extends JpaRepository<Race, Long> {
  List<Race> findAllByActiveTrue();

  @Query("select r from Race r left join fetch r.results re left join fetch re.participant where r.active")
  List<Race> findAllActiveRacesWithResults();

  @Query("select r from Race r left join fetch r.results re left join fetch re.participant left join fetch re.answers a left join fetch a.question where r.id = :raceId")
  Optional<Race> findByIdWithResults(@Param("raceId") Long raceId);
}
