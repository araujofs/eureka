package br.edu.ifpb.pweb2.eureka.question;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository repo;

  public Optional<Question> getById(Long id) {
    return repo.findById(id);
  }    
}
