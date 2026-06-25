package br.edu.ifpb.pweb2.eureka.ranking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.ranking.dto.RankDto;
import br.edu.ifpb.pweb2.eureka.ranking.dto.UserRankDto;
import br.edu.ifpb.pweb2.eureka.result.Result;
import br.edu.ifpb.pweb2.eureka.result.ResultRepository;
import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;
import br.edu.ifpb.pweb2.eureka.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {

  private final UserRepository userRepo;
  private final ResultRepository resultRepo;
  private final UserService userService;

  public RankDto<User> getOverallRanking(Long userId, Pageable pageable) {
    Page<User> users = userRepo.findAll(pageable);
    List<UserRankDto> ranks = new ArrayList<>();
    UserRankDto currUserRank = null;

    int offset = users.getNumber() * users.getSize();

    for (int i = 0; i < users.getNumberOfElements(); i++) {
      User user = users.getContent().get(i);
      UserRankDto userRank = new UserRankDto(user.getId(), user.getTotalPoints(), offset + i, user.getName(), null);
      ranks.add(userRank);

      if (userId.equals(user.getId())) {
        currUserRank = userRank;
      }
    }

    return new RankDto<User>(ranks, currUserRank, true, users);
  }

  public RankDto<Result> getRankingByRace(Race race, Long userId, Pageable pageable) {
    Page<Result> results = resultRepo.findByRace(race, pageable);
    List<UserRankDto> ranks = new ArrayList<>();
    UserRankDto currUserRank = null;

    int offset = results.getNumber() * results.getSize();

    for (int i = 0; i < results.getNumberOfElements(); i++) {
      Result result = results.getContent().get(i);
      User user = result.getParticipant();

      UserRankDto userRank = new UserRankDto(user.getId(), result.getTotalPoints(), i + offset, user.getName(), result.getFinishedRaceAt());
      ranks.add(userRank);

      if (userId.equals(user.getId())) {
        currUserRank = userRank;
      }
    }

    return new RankDto<Result>(ranks, currUserRank, true, results);
  }
}
