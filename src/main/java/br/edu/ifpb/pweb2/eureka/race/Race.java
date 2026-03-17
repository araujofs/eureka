package br.edu.ifpb.pweb2.eureka.race;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.edu.ifpb.pweb2.eureka.question.Question;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Race {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String title;

  @Column
  private String description;

  @Column
  private int duration;

  @OneToMany(mappedBy = "race", fetch = FetchType.LAZY, cascade = {
      CascadeType.REMOVE,
      CascadeType.PERSIST
  }, orphanRemoval = true)
  private Set<Question> questions = new HashSet<>();

  public void addQuestion(Question q) {
    Objects.requireNonNull(q, "Question argument must not be null");

    q.setRace(this);
    questions.add(q);
  }

  public void removeQuestion(Question q) {
    Objects.requireNonNull(q, "Question argument must not be null");

    if (!questions.contains(q)) {
      throw new IllegalArgumentException("Question does not belong to Race");
    }

    q.setRace(null);
    questions.remove(q);
  }
}
