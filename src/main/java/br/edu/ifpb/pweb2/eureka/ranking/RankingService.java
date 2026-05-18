package br.edu.ifpb.pweb2.eureka.ranking;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.ranking.dto.RankDto;
import br.edu.ifpb.pweb2.eureka.ranking.dto.UserRankDto;
import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {

  private final UserService userService;

  class RankComparator implements Comparator<UserRankDto> {
    @Override
    public int compare(UserRankDto rank1, UserRankDto rank2) {
      return rank2.getPoints() - rank1.getPoints();
    }
  }

  public RankDto getRanking(Long userId) {
    List<User> users = userService.getAllWithResults();
    UserRankDto currUser = new UserRankDto();

    List<UserRankDto> userRanks = users.stream().map(user -> {
      int userPoints = user.getResults().stream().mapToInt(result -> result.getPoints()).sum();
      var userRank = new UserRankDto(user.getId(), userPoints, 0, user.getName());

      if (user.getId() == userId) {
        currUser.setPoints(userPoints);
        currUser.setRanking(0);
        currUser.setUserName(user.getName());
      }

      return userRank;
    }).collect(Collectors.toCollection(ArrayList::new));

    userRanks.sort(new RankComparator());

    IntStream.range(0, userRanks.size()).forEach(i -> {
      var rank = userRanks.get(i);
      System.out.println("Rank Id: " + rank.getId());

      if (rank.getId() == userId) {
        currUser.setRanking(i);
      }

      rank.setRanking(i);
    });

    return new RankDto(userRanks, currUser);
  }

  public RankDto getRanking(Long userId, Race race) {
    UserRankDto currUser = new UserRankDto();
    currUser.setId(userId);

    List<UserRankDto> userRanks = race.getResults().stream().map(result -> {
      var user = result.getParticipant();
      int userPoints = result.getPoints();
      var userRank = new UserRankDto(user.getId(), userPoints, 0, user.getName());

      if (user.getId() == userId) {
        currUser.setPoints(userPoints);
        currUser.setRanking(0);
        currUser.setUserName(user.getName());
      }

      return userRank;
    }).collect(Collectors.toCollection(ArrayList::new));

    userRanks.sort(new RankComparator());

    IntStream.range(0, userRanks.size()).forEach(i -> {
      var rank = userRanks.get(i);
      System.out.println("Rank Id: " + rank.getId());

      if (rank.getId() == userId) {
        currUser.setRanking(i);
      }

      rank.setRanking(i);
    });

    return new RankDto(userRanks, currUser);
  }
}
