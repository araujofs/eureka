package br.edu.ifpb.pweb2.eureka.ranking.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankDto<T> {
  private List<UserRankDto> rank;
  private UserRankDto currentRankDto;
  private boolean general;
  private Page<T> page;
}
