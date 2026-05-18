package br.edu.ifpb.pweb2.eureka.race;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {
  List<Race> findAllByActiveTrue();

  @EntityGraph(attributePaths = {"results", "results.participant"})
  List<Race> findAllByActiveTrueOrderByResultsParticipantNameAsc();

  @EntityGraph(attributePaths = {"results", "results.participant", "results.answers", "results.answers.question"})
  Optional<Race> findByIdOrderByResultsParticipantNameAsc(Long raceId);
}
