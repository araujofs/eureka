package br.edu.ifpb.pweb2.eureka.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import br.edu.ifpb.pweb2.eureka.result.Result;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique = true, nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean admin = false;

  @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Result> results = new HashSet<>();

  @Column(nullable = false)
  private int totalPoints;

  public void addResult(Result r) {
    Objects.requireNonNull(r, "Question argument must not be null");

    r.setParticipant(this);
    results.add(r);
    totalPoints += r.getTotalPoints();
  }

  public void removeResult(Result r) {
    Objects.requireNonNull(r, "Result argument must not be null");

    if (!results.contains(r)) {
      throw new IllegalArgumentException("Result does not belong to Race");
    }

    results.remove(r);
    r.setRace(null);
    totalPoints -= r.getTotalPoints();
  }
}
