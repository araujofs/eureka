package br.edu.ifpb.pweb2.eureka.race;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.question.dto.QuestionCreateDto;
import br.edu.ifpb.pweb2.eureka.question.dto.QuestionMapper;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceMapper;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceMapperContext;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceQuestionsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RaceService {
  private final RaceRepository repo;

  public Long create(RaceCreateDto dto) {
    Objects.requireNonNull(dto, "Race must not be null to save it!");
    Race race = RaceMapper.INSTANCE.toEntity(dto, new RaceMapperContext());

    // will never be null
    return (repo.save(race)).getId();
  }

  public boolean edit(RaceDto dto) {
    Objects.requireNonNull(dto, "Race must not be null to save it!");
    var race = repo.findById(dto.getId())
        .orElseThrow(() -> new IllegalArgumentException("No race found with id: " + dto.getId()));

    race.setTitle(dto.getTitle());
    race.setDescription(dto.getDescription());
    race.setDuration(dto.getDuration());
    repo.save(race);
    return true;
  }

  public void addQuestions(List<QuestionCreateDto> dtos, Long raceId) {
    Objects.requireNonNull(dtos, "Questions must not be null to add it!");
    Objects.requireNonNull(raceId, "RaceId must not be null to find it!");

    var race = repo.findById(raceId)
        .orElseThrow(() -> new IllegalArgumentException("No race found with raceId: " + raceId));

    if (dtos.size() == 0) {
      race.getQuestions().stream().forEach(question -> {
        race.removeQuestion(question);
      });
      fixActive(race);
      repo.save(race);
      return;
    }

    var questions = QuestionMapper.INSTANCE.map(dtos);

    race.getQuestions().forEach(question -> {
      if (!questions.contains(question)) {
        race.removeQuestion(question);
      }
    });

    questions.forEach(question -> {
      if (question.getId() != null) {
        race.removeQuestion(question);
      }

      race.addQuestion(question);
    });

    fixActive(race);
    repo.save(race);
  }

  public List<RaceDto> getAll() {
    var races = repo.findAll();
    return RaceMapper.INSTANCE.toDtos(races);
  }

  public Optional<Race> getById(Long id) {
    return repo.findById(id);
  }

  public Optional<RaceDto> getCreateById(Long id) {
    var race = repo.findById(id);

    if (race == null) {
      Optional.empty();
    }
    return Optional.of(RaceMapper.INSTANCE.toDto(race.get()));
  }

  public Optional<RaceQuestionsDto> getQuestionsDtoById(Long id) {
    var race = repo.findById(id);
    if (race == null) {
      return Optional.empty();
    }

    var raceQuestions = new RaceQuestionsDto();
    raceQuestions.setId(race.get().getId());

    var questionDtos = race.get().getQuestions().stream().map(question -> {
      var questionDto = new QuestionCreateDto();
      questionDto.setId(question.getId());
      questionDto.setAnswers(question.getAnswers());
      questionDto.setCorrectAnswer(question.getCorrectAnswer());
      questionDto.setDifficulty(question.getDifficulty());
      questionDto.setStatement(question.getStatement());

      return questionDto;
    }).toList();
    raceQuestions.setQuestions(questionDtos);

    return Optional.of(raceQuestions);
  }

  public void remove(Long id) {
    var race = repo.findById(id);
    Objects.requireNonNull(race, "Race must exist to be deleted");

    repo.deleteById(id);
  }

  private void fixActive(Race race) {
    if (race.getQuestions().size() == 0) {
      race.setActive(false);
      return;
    }

    race.setActive(true);
  }
}
