package br.edu.ifpb.pweb2.eureka.race;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RaceRepository extends JpaRepository<Race, Long> {
  List<Race> findAllByActiveTrue();

  @Query("select r from Race r left join fetch r.results re left join fetch re.participant where r.active")
  List<Race> findAllActiveRacesWithResults();
}
