package br.edu.ifpb.pweb2.eureka.question.difficulty;

import java.util.stream.Stream;

public enum Difficulty {
  EASY(1), MEDIUM(3), HARD(5);

  private Integer value;

  Difficulty(Integer difficulty) {
    this.value = difficulty;
  }

  public int getValue() {
    return this.value;
  }

  public static Difficulty fromValue(Integer value) {
    return Stream.of(values()).filter(val -> val.value.equals(value)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("ActionType inválido: " + value));
  }
}
