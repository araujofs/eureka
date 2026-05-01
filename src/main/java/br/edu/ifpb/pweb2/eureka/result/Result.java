package br.edu.ifpb.pweb2.eureka.result;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttempt;
import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"participant_id", "race_id"})})
public class Result {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "participant_id", nullable = false)
  private User participant;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "race_id", nullable = false)
  private Race race;

  @OneToMany(mappedBy = "result", fetch = FetchType.LAZY, cascade = {
      CascadeType.REMOVE,
      CascadeType.PERSIST
  }, orphanRemoval = true)
  private Set<AnswerAttempt> answers = new HashSet<>();

  @CreationTimestamp
  @Column(nullable = false, updatable = false, insertable = false)
  private LocalDateTime startedRaceAt;

  private LocalDateTime finishedRaceAt;

  private Long currentQuestionId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Result))
      return false;

    var a = (Result) o;
    return id != null && id.equals(a.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public void addAnswer(AnswerAttempt q) {
    Objects.requireNonNull(q, "AnsweredQuestion argument must not be null");

    q.setResult(this);
    answers.add(q);
  }

  public void removeAnswer(AnswerAttempt q) {
    Objects.requireNonNull(q, "AnsweredQuestion argument must not be null");

    if (!answers.contains(q)) {
      throw new IllegalArgumentException("AnsweredAnsweredQuestion does not belong to Race");
    }

    q.setResult(null);
    answers.remove(q);
  }

  public Integer getPoints() {
    return getAnswers().stream().filter(answer -> answer.isAnswerCorrect())
        .mapToInt(answer -> answer.getQuestion().getDifficulty().getValue()).sum();
  }
}
