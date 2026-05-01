package br.edu.ifpb.pweb2.eureka.race;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.result.Result;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Race {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String title;

  private String description;

  private Integer duration;

  private boolean active = false;

  @OneToMany(mappedBy = "race", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions = new ArrayList<>();

  @OneToMany(mappedBy = "race", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Result> results = new HashSet<>();

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

  public void addResult(Result r) {
    Objects.requireNonNull(r, "Question argument must not be null");

    r.setRace(this);
    results.add(r);
  }

  public void removeResult(Result r) {
    Objects.requireNonNull(r, "Result argument must not be null");

    if (!results.contains(r)) {
      throw new IllegalArgumentException("Result does not belong to Race");
    }

    r.setRace(null);
    results.remove(r);
  }
}
