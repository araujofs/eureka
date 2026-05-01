package br.edu.ifpb.pweb2.eureka.question.dto;

import java.util.List;

import br.edu.ifpb.pweb2.eureka.question.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QuestionCheckDto {

  private Question question;
  private List<Long> questionsIds;
  private String errorMessage;
  private String redirectUrl;
}
