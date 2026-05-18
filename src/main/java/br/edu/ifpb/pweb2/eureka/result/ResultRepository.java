package br.edu.ifpb.pweb2.eureka.result;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.user.User;

public interface ResultRepository extends JpaRepository<Result, Long> {
  Optional<Result> findByParticipantAndRace(User participant, Race race);
}
