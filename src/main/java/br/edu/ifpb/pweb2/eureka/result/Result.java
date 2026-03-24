package br.edu.ifpb.pweb2.eureka.result;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

// associative entity between user and race
// will have a separate ID and unique constraints
@Entity
@Getter
@Setter
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
  private Set<AnsweredQuestion> answeredQuestions = new HashSet<>();

  private Long points;

  private LocalDateTime answeredAt;

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

  public void addAnsweredAnsweredQuestion(AnsweredQuestion q) {
    Objects.requireNonNull(q, "AnsweredQuestion argument must not be null");

    q.setResult(this);
    answeredQuestions.add(q);
  }

  public void removeAnsweredAnsweredQuestion(AnsweredQuestion q) {
    Objects.requireNonNull(q, "AnsweredQuestion argument must not be null");

    if (!answeredQuestions.contains(q)) {
      throw new IllegalArgumentException("AnsweredAnsweredQuestion does not belong to Race");
    }

    q.setResult(null);
    answeredQuestions.remove(q);
  }
}
