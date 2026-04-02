package br.edu.ifpb.pweb2.eureka.question.difficulty;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DifficultyConverter implements AttributeConverter<Difficulty, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Difficulty attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public Difficulty convertToEntityAttribute(Integer dbData) {
    return dbData == null ? null : Difficulty.fromValue(dbData);
  }

}
