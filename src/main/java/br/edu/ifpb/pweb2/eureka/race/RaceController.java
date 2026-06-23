package br.edu.ifpb.pweb2.eureka.race;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.eureka.config.security.CustomUserDetails;
import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.question.QuestionService;
import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttempt;
import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttemptService;
import br.edu.ifpb.pweb2.eureka.question.dto.QuestionCheckDto;
import br.edu.ifpb.pweb2.eureka.question.dto.attempt.AnswerAttemtCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceEditDto;
import br.edu.ifpb.pweb2.eureka.result.ResultService;
import br.edu.ifpb.pweb2.eureka.result.dto.ResultCheckDto;
import br.edu.ifpb.pweb2.eureka.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/race")
@RequiredArgsConstructor
public class RaceController {

  public static final String RESULT_SESSION_ATTR = "resultId";
  public static final String RACE_SESSION_ATTR = "raceId";
  public static final String QUESTIONS_SESSION_ATTR = "questionsIds";
  public static final String ANSWER_SESSION_ATTR = "questionsIdxs";

  private final RaceService raceService;
  private final UserService userService;
  private final ResultService resultService;
  private final QuestionService questionService;
  private final AnswerAttemptService answerAttemptService;

  public void print(Object value) {
    System.out.println(value);
  }

  @GetMapping("/create")
  public String getRaceForm(Model model) {
    if (!model.containsAttribute("race"))
      model.addAttribute("race", new RaceCreateDto());

    return "race/form";
  }

  @GetMapping("/confirm")
  public String getConfirm(Model model) {
    if (!model.containsAttribute("raceId"))
      return "redirect:/home";

    return "race/confirm";
  }

  @PostMapping("/create")
  public String postRaceForm(RaceCreateDto newRace, RedirectAttributes attributes) {

    try {
      attributes.addFlashAttribute("raceId", raceService.create(newRace));
      return "redirect:/race/confirm";
    } catch (Exception e) {
      attributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, e.getLocalizedMessage());
      attributes.addFlashAttribute("race", newRace);
    }

    return "redirect:/race/create";
  }

  @GetMapping("/edit/{id}")
  public String getRaceEditForm(@PathVariable Long id, Model model) {
    var race = raceService.getById(id);
    model.addAttribute("race", race.get());

    return "race/edit/form";
  }

  @PostMapping("/edit/{id}")
  public String postRaceEditForm(@PathVariable Long id, RaceEditDto newRace, RedirectAttributes attributes) {
    try {
      attributes.addFlashAttribute("raceId", raceService.edit(newRace));
      return "redirect:/home";
    } catch (Exception e) {
      attributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, e.getLocalizedMessage());
      attributes.addFlashAttribute("race", newRace);
    }

    return "redirect:/race/edit/" + id;
  }

  @GetMapping("/delete/{id}")
  public String getDeleteConfirm(@PathVariable Long id, Model model) {
    model.addAttribute("raceId", id);

    return "race/delete/confirm";
  }

  @DeleteMapping("/delete/{id}")
  public String deleteRace(@PathVariable Long id, RedirectAttributes attributes) {
    try {
      raceService.remove(id);
    } catch (Exception e) {
      attributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, e.getMessage());
      return "redirect:/race/delete/{id}";
    }

    return "redirect:/home";
  }

  @GetMapping("/{id}/run/confirm")
  public String raceInitConfirmTemplate(@PathVariable Long id, Model model) {
    model.addAttribute("raceId", id);

    return "race/run-confirm";
  }

  @PostMapping("/{id}/run")
  public String raceRun(@PathVariable Long id, HttpSession session, RedirectAttributes flashAttributes, @AuthenticationPrincipal CustomUserDetails userDetails) {
    var user = userService.getById((Long) userDetails.getUserId()).orElse(null);
    if (user == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR,
          "Seu usuário não existe e portanto não pode participar de corridas!");
      return "redirect:/home";
    }

    var race = raceService.getById(id).orElse(null);
    if (race == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, "Corrida não existe!");
      return "redirect:/home";
    }

    var result = resultService.create(user, race);

    session.setAttribute(RACE_SESSION_ATTR, race.getId());
    session.setAttribute(RESULT_SESSION_ATTR, result.getId());
    session.setAttribute(QUESTIONS_SESSION_ATTR,
        race.getQuestions().stream().map(Question::getId).collect(Collectors.toCollection(ArrayList::new)));

    return "redirect:/race/" + id + "/running";
  }

  @GetMapping("/{id}/running")
  public String raceRunningTemplate(@PathVariable Long id, Model model, HttpSession session,
      RedirectAttributes flashAttributes) {
    session.removeAttribute(ANSWER_SESSION_ATTR);

    var questionCheck = hasQuestion(session, id);
    var question = questionCheck.getQuestion();
    if (question == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, questionCheck.getErrorMessage());
      return "redirect:" + questionCheck.getRedirectUrl();
    }

    var checkResult = hasResult(session, id);
    var result = checkResult.getResult();
    if (result == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, checkResult.getErrorMessage());
      return "redirect:" + checkResult.getRedirectUrl();
    }

    if (LocalDateTime.now().isAfter(result.getStartedRaceAt().plusSeconds(result.getRace().getDuration()))) {
      flashAttributes.addFlashAttribute("message", "O tempo acabou!");
      return "redirect:/race/" + id + "/result";
    }

    result.setCurrentQuestionId(
        questionCheck.getQuestionsIds().size() > 0 ? questionCheck.getQuestionsIds().getFirst() : null);
    resultService.edit(result);

    if (questionCheck.getQuestionsIds().size() <= 0) {
      return "redirect:/race/" + id + "/result";
    }

    model.addAttribute("question", question);
    model.addAttribute("resultId", result.getId());

    return "race/question";
  }

  @PostMapping("/{id}/answer")
  public String verifyAnswer(AnswerAttemtCreateDto answerAttempt, @PathVariable Long id, HttpSession session,
      RedirectAttributes flashAttributes) {
    var resultCheck = hasResult(session, id);
    var result = resultCheck.getResult();
    if (result == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, resultCheck.getErrorMessage());
      return "redirect:" + resultCheck.getRedirectUrl();
    }

    if (Duration.between(result.getStartedRaceAt(), LocalDateTime.now()).getSeconds() >= result.getRace()
        .getDuration()) {
      flashAttributes.addFlashAttribute("message", "O tempo acabou!");
      return "redirect:/race/" + id + "/result";
    }

    var questionCheck = hasQuestion(session, id);
    var question = questionCheck.getQuestion();
    if (question == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, questionCheck.getErrorMessage());
      return "redirect:" + questionCheck.getRedirectUrl();
    }

    if (question.getId() != answerAttempt.getQuestionId()) {
      questionCheck.setErrorMessage("Você está tentando responder a pergunta errada!");
      return "redirect:/race/" + id + "/running";
    }

    var answer = answerAttemptService.create(answerAttempt.getAnswerIndex(), question);
    result.addAnswer(answer);

    questionCheck.getQuestionsIds().removeFirst();
    result = resultService.saveAndFlush(result);
    answer = result.getAnswers().get(result.getAnswers().indexOf(answer));

    session.setAttribute(ANSWER_SESSION_ATTR, answer.getId());
    session.setAttribute(QUESTIONS_SESSION_ATTR, questionCheck.getQuestionsIds());

    return "redirect:/race/" + id + "/answer";
  }

  @GetMapping("/{id}/answer")
  public String checkAnswerPage(@PathVariable Long id, Model model, HttpSession session,
      RedirectAttributes flashAttributes) {

    if (session.getAttribute(ANSWER_SESSION_ATTR) == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, "Ainda não iniciou corrida!");
      return "redirect:/home";
    }
    AnswerAttempt answer = answerAttemptService.getById((Long) session.getAttribute(ANSWER_SESSION_ATTR)).get();
    model.addAttribute("question", answer.getQuestion());
    model.addAttribute("userAnswer", answer.getAnswerIndex());

    var questions = (List<Long>) session.getAttribute(QUESTIONS_SESSION_ATTR);
    model.addAttribute("finalQuestion", questions == null || questions.size() <= 0);

    return "race/answer";
  }

  @GetMapping("/{id}/result")
  public String result(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes flashAttributes) {
    var resultCheck = hasResult(session, id);
    var result = resultCheck.getResult();
    if (result == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, resultCheck.getErrorMessage());
      return "redirect:" + resultCheck.getRedirectUrl();
    }

    result.setFinishedRaceAt(LocalDateTime.now());

    resultService.saveAndFlush(result);

    session.removeAttribute(ANSWER_SESSION_ATTR);
    session.removeAttribute(QUESTIONS_SESSION_ATTR);
    session.removeAttribute(RESULT_SESSION_ATTR);
    session.removeAttribute(RACE_SESSION_ATTR);

    model.addAttribute("points", result.getPoints());
    model.addAttribute("answers", result.getAnswers());
    model.addAttribute("time", Duration.between(result.getStartedRaceAt(), result.getFinishedRaceAt()).getSeconds());

    return "race/result";
  }

  private ResultCheckDto hasResult(HttpSession session, Long raceId) {
    var resultCheck = new ResultCheckDto(null, null, "/home");

    var resultId = (Long) session.getAttribute(RESULT_SESSION_ATTR);
    if (resultId == null) {
      resultCheck.setErrorMessage("Você não está jogando essa corrida!");
      return resultCheck;
    }

    var result = resultService.getById(resultId).get();

    if (raceId == null || result.getRace().getId() != raceId) {
      resultCheck.setErrorMessage("Ainda não iniciou corrida (has)!");
      return resultCheck;
    }

    resultCheck.setResult(result);
    return resultCheck;
  }

  private QuestionCheckDto hasQuestion(HttpSession session, Long raceId) {
    var questionCheck = new QuestionCheckDto(null, null, null, "/home");

    var questionsIds = (ArrayList<Long>) session.getAttribute(QUESTIONS_SESSION_ATTR);
    print("QuestionsIds: " + questionsIds);

    if (questionsIds == null) {
      questionCheck.setErrorMessage("Você não está jogando uma corrida!");
      return questionCheck;
    }

    if (questionsIds.size() == 0) {
      questionCheck.setQuestionsIds(questionsIds);
      return questionCheck;
    }

    var questionId = questionsIds.getFirst();
    var question = questionService.getById(questionId).get();
    if (raceId == null || question.getRace().getId() != raceId) {
      questionCheck.setErrorMessage("Você não está jogando essa corrida!");
      return questionCheck;
    }

    questionCheck.setQuestion(question);
    questionCheck.setQuestionsIds(questionsIds);
    return questionCheck;
  }

}
