package br.edu.ifpb.pweb2.eureka.question.attempt;

import java.util.Objects;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.result.Result;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"result_id", "question_id"})})
@lombok.ToString
public class AnswerAttempt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "result_id", nullable = false)
  private Result result;

  // unidirectional
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Question question;

  @Column(nullable = false, updatable = false)
  private Integer answerIndex;

  @Column(nullable = false, updatable = false)
  private boolean answerCorrect;

  @Column(nullable = false)
  private int pointsReceived = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AnswerAttempt))
      return false;

    var a = (AnswerAttempt) o;
    return Objects.equals(result, a.result) && Objects.equals(question, a.question);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
