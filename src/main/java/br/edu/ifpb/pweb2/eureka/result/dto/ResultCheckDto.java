package br.edu.ifpb.pweb2.eureka.result.dto;

import br.edu.ifpb.pweb2.eureka.result.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultCheckDto {

  private Result result;
  private String errorMessage;
  private String redirectUrl;
}
