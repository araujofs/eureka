package br.edu.ifpb.pweb2.eureka.question.dto.attempt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerAttemtCreateDto {

  private Long resultId;
  private Long questionId;
  private Integer answerIndex;
}
