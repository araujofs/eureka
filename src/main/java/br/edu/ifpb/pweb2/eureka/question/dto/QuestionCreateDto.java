package br.edu.ifpb.pweb2.eureka.question.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.edu.ifpb.pweb2.eureka.question.Image;
import br.edu.ifpb.pweb2.eureka.question.ImageAction;
import br.edu.ifpb.pweb2.eureka.question.difficulty.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDto {
  private Long id;
  private String statement;
  private Difficulty difficulty;
  private List<String> answers = new ArrayList<>();
  private Integer correctAnswer;
  private MultipartFile imageUpload;
  private ImageAction imageAction;
  private Image image;
}
