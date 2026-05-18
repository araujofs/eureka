package br.edu.ifpb.pweb2.eureka.ranking.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankDto {
  private List<UserRankDto> rank;
  private UserRankDto currentRankDto;
}
