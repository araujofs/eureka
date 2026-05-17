package br.edu.ifpb.pweb2.eureka.question.dto.attempt;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerAttemtCreateDto {

  private Long resultId;
  private Long questionId;
  private Integer answerIndex;
}
