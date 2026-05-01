package br.edu.ifpb.pweb2.eureka.question.attempt;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.question.Question;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerAttemptService {

  private final AnswerAttemptRepository repo;
  
  public AnswerAttempt save(AnswerAttempt answer) {
    return repo.save(answer);
  }

  public Optional<AnswerAttempt> getById(Long id) {
    return repo.findById(id);
  }    

  public AnswerAttempt create(Integer answerIndex, Question question) {
    var answer = new AnswerAttempt();
    answer.setAnswerIndex(answerIndex);
    answer.setQuestion(question);
    answer.setAnswerCorrect(answerIndex != question.getCorrectAnswer());

    return answer;
  }
}
