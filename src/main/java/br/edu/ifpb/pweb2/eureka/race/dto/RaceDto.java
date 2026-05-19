package br.edu.ifpb.pweb2.eureka.race.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RaceDto {

  private Long id;
  private String title;
  private String description;
  private Integer duration;
  private boolean active;
  private boolean played;
  private int questionsSize;
}
