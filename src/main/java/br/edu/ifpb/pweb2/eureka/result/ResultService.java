package br.edu.ifpb.pweb2.eureka.result;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.user.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultService {

  private final ResultRepository repo;

  public Result create(User participant, Race race) {
    var result = new Result();
    result.setParticipant(participant);
    result.setRace(race);

    return repo.save(result);
  }

  public Result saveAndFlush(Result result) {
    return repo.saveAndFlush(result);
  }

  public Result edit(Result result) {
    return repo.save(result);
  }

  public Optional<Result> getById(Long id) {
    return repo.findById(id);
  }
}
