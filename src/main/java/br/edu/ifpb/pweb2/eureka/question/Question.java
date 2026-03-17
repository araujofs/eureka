package br.edu.ifpb.pweb2.eureka.question;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.edu.ifpb.pweb2.eureka.race.Race;
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
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String statement;

  @Column
  private int points;

  @ManyToOne(optional = false)
  @JoinColumn(name = "race_id", nullable = false)
  private Race race;

  @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = {
      CascadeType.REMOVE,
      CascadeType.PERSIST
  }, orphanRemoval = true)
  private Set<Answer> answers = new HashSet<>();

  public void setRace(Race r) {
    if (r == null) {
      throw new IllegalArgumentException("Race argument must not be null");
    }
  }

  public void addAnswer(Answer a) {
    Objects.requireNonNull(a, "Answer argument must not be null");

    a.setQuestion(this);
    answers.add(a);
  }

  public void removeAnswer(Answer a) {
    Objects.requireNonNull(a, "Answer argument must not be null");

    if (!answers.contains(a)) {
      throw new IllegalArgumentException("Answer does not belong to Question");
    }

    a.setQuestion(null);
    answers.remove(a);
  }
}
