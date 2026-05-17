package br.edu.ifpb.pweb2.eureka.question;

import java.util.List;
import java.util.Objects;

import br.edu.ifpb.pweb2.eureka.question.difficulty.Difficulty;
import br.edu.ifpb.pweb2.eureka.race.Race;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"race", "statement"})})
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String statement;

  @Column
  private Difficulty difficulty;

  @ElementCollection
  private List<String> answers;

  private Integer correctAnswer;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "race_id", nullable = false)
  private Race race;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Question))
      return false;

    var a = (Question) o;
    return Objects.equals(race, a.race) && Objects.equals(statement, a.statement);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
