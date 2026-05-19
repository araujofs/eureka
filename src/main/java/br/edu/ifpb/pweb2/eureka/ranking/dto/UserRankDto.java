package br.edu.ifpb.pweb2.eureka.ranking.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRankDto {
  private Long id;
  private int points;
  private Integer ranking;
  private String userName;
  private LocalDateTime answeredAt;
}
