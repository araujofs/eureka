package br.edu.ifpb.pweb2.eureka.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String content;

  @Column
  private boolean correct;

  @ManyToOne(optional = false)
  @JoinColumn(name = "question_id", nullable = false)
  private Question question;

  public void setQuestion(Question q) {
    if (q == null) {
      throw new IllegalArgumentException("Question argument must not be null");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Answer))
      return false;

    Answer a = (Answer) o;
    return id != null && id.equals(a.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
