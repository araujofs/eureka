package br.edu.ifpb.pweb2.eureka.question.attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerAttemptRepository extends JpaRepository<AnswerAttempt, Long> {
    
}
