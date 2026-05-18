package br.edu.ifpb.pweb2.eureka.race.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RaceEditDto {
  private Long id;
  private String title;
  private String description;
  private Integer duration;
}
