package br.edu.ifpb.pweb2.eureka.race;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.pweb2.eureka.question.QuestionService;
import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttempt;
import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttemptService;
import br.edu.ifpb.pweb2.eureka.question.dto.QuestionCheckDto;
import br.edu.ifpb.pweb2.eureka.question.dto.attempt.AnswerAttemtCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceCreateDto;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceDto;
import br.edu.ifpb.pweb2.eureka.result.ResultService;
import br.edu.ifpb.pweb2.eureka.result.dto.ResultCheckDto;
import br.edu.ifpb.pweb2.eureka.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/race")
@RequiredArgsConstructor
public class RaceController {

  private final RaceService raceService;
  private final UserService userService;
  private final ResultService resultService;
  private final QuestionService questionService;
  private final AnswerAttemptService answerAttemptService;
  public static final String RESULT_SESSION_ATTR = "resultId";
  public static final String RACE_SESSION_ATTR = "raceId";
  public static final String QUESTIONS_SESSION_ATTR = "questionsIds";
  public static final String ANSWER_SESSION_ATTR = "questionsIdxs";

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
  public String postRaceEditForm(@PathVariable Long id, RaceDto newRace, RedirectAttributes attributes) {
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
  public String raceRun(@PathVariable Long id, HttpSession session, RedirectAttributes flashAttributes) {
    var user = userService.getById((Long) session.getAttribute("userId"));
    if (user.isEmpty()) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR,
          "Seu usuário não existe e portanto não pode participar de corridas!");
      return "redirect:/home";
    }

    var race = raceService.getById(id);
    if (race.isEmpty()) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, "Corrida não existe!");
      return "redirect:/home";
    }

    var result = resultService.create(user.get(), race.get());

    session.setAttribute(RACE_SESSION_ATTR, race.get().getId());
    session.setAttribute(RESULT_SESSION_ATTR, result.getId());
    session.setAttribute(QUESTIONS_SESSION_ATTR,
        race.get().getQuestions().stream().map(question -> question.getId()).toList());

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

    model.addAttribute("question", question);
    model.addAttribute("resultId", result.getId());

    // the form data should be like answerAttemptCreateDto
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

    var questionCheck = hasQuestion(session, answerAttempt.getQuestionId(), id);
    var question = questionCheck.getQuestion();
    if (question == null) {
      flashAttributes.addFlashAttribute(HomeController.ERROR_MESSAGE_MODEL_ATTR, questionCheck.getErrorMessage());
      return "redirect:" + questionCheck.getRedirectUrl();
    }

    var answer = answerAttemptService.create(answerAttempt.getAnswerIndex(), question);
    result.addAnswer(answer);

    if (questionCheck.getQuestionsIds().size() <= 0) {
      result.setCurrentQuestionId(null);
      result.setFinishedRaceAt(LocalDateTime.now());
      resultService.saveAndFlush(result);

      session.removeAttribute(ANSWER_SESSION_ATTR);
      session.removeAttribute(QUESTIONS_SESSION_ATTR);

      return "redirect:/race/" + id + "/result";
    }

    result.setCurrentQuestionId(questionCheck.getQuestionsIds().getFirst());
    resultService.saveAndFlush(result);

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
    model.addAttribute("points", result.getPoints());
    model.addAttribute("answers", result.getAnswers());

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
      resultCheck.setErrorMessage("Ainda não iniciou corrida!");
      return resultCheck;
    }

    resultCheck.setResult(result);
    return resultCheck;
  }

  private QuestionCheckDto hasQuestion(HttpSession session, Long answerAttemptQuestionId, Long raceId) {
    var questionCheck = new QuestionCheckDto(null, null, null, "/home");

    var questionsIds = (List<Long>) session.getAttribute(QUESTIONS_SESSION_ATTR);
    if (questionsIds == null) {
      questionCheck.setErrorMessage("Você não está jogando uma corrida!");
      return questionCheck;
    }

    var questionId = questionsIds.removeFirst();
    var question = questionService.getById(questionId).get();
    if (raceId == null || question.getRace().getId() != raceId) {
      questionCheck.setErrorMessage("Você não está jogando essa corrida!");
      return questionCheck;
    }

    if (questionId != answerAttemptQuestionId) {
      questionCheck.setErrorMessage("Você está tentando responder a pergunta errada!");
      return questionCheck;
    }

    questionCheck.setQuestion(question);
    questionCheck.setQuestionsIds(questionsIds);
    return questionCheck;
  }

  private QuestionCheckDto hasQuestion(HttpSession session, Long raceId) {
    var questionCheck = new QuestionCheckDto(null, null, null, "/home");

    var questionsIds = (List<Long>) session.getAttribute(QUESTIONS_SESSION_ATTR);
    if (questionsIds == null) {
      questionCheck.setErrorMessage("Você não está jogando uma corrida!");
      return questionCheck;
    }

    var questionId = questionsIds.removeFirst();
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
