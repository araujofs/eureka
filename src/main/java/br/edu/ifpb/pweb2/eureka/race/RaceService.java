package br.edu.ifpb.pweb2.eureka.race;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.question.Image;
import br.edu.ifpb.pweb2.eureka.question.ImageAction;
import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.question.QuestionService;
import br.edu.ifpb.pweb2.eureka.question.dto.QuestionCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceEditDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceMapper;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceMapperContext;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceQuestionsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RaceService {
  private final RaceRepository repo;
  private final QuestionService questionService;

  public Long create(RaceCreateDto dto) {
    Objects.requireNonNull(dto, "Race must not be null to save it!");
    Race race = RaceMapper.INSTANCE.toEntity(dto, new RaceMapperContext());

    // will never be null
    return (repo.save(race)).getId();
  }

  public boolean edit(RaceEditDto dto) {
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

    race.getQuestions().clear();

    if (dtos.size() == 0) {
      fixActive(race);
      repo.save(race);
      return;
    }

    List<Question> questions = dtos.stream().map(dto -> {
      Question question = new Question();
      question.setId(dto.getId());
      question.setStatement(dto.getStatement());
      question.setDifficulty(dto.getDifficulty());
      question.setAnswers(dto.getAnswers());
      question.setCorrectAnswer(dto.getCorrectAnswer());
      question.setRace(race);
      Image image;

      try {
        image = questionService.getImageFromDto(dto);
      } catch (Exception e) {
        image = null;
      }
      question.setImage(image);

      return question;
    }).collect(Collectors.toCollection(ArrayList::new));

    questions.forEach(question -> {
      race.addQuestion(question);
    });

    fixActive(race);
    repo.save(race);
  }

  public List<RaceDto> getAll() {
    var races = repo.findAll();
    return RaceMapper.INSTANCE.toDtos(races);
  }

  public List<RaceDto> getAllActive(Long userId) {
    var races = repo.findAllByActiveTrueOrderByResultsParticipantNameAsc();
    var raceDtos = races.stream().map(race -> {
      var played = race.getResults().stream().anyMatch(result -> result.getParticipant().getId() == userId);
      return new RaceDto(race.getId(), race.getTitle(), race.getDescription(), race.getDuration(), race.isActive(),
          played, race.getQuestionsSize());
    });

    return raceDtos.toList();
  }

  public Optional<Race> getById(Long id) {
    return repo.findById(id);
  }

  public Optional<Race> getByIdWithResults(Long id) {
    return repo.findByIdOrderByResultsParticipantNameAsc(id);
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
      var questionDto = new QuestionCreateDto(
          question.getId(),
          question.getStatement(),
          question.getDifficulty(),
          question.getAnswers(),
          question.getCorrectAnswer(),
          null,
          question.getImage() != null ? ImageAction.KEEP : ImageAction.NONE,
          question.getImage());

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
