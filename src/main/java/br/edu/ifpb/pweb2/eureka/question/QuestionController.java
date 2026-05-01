package br.edu.ifpb.pweb2.eureka.question;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.pweb2.eureka.question.dto.QuestionAttemptDto;
import br.edu.ifpb.pweb2.eureka.race.RaceService;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceQuestionsDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final RaceService raceService;

  @GetMapping("/add")
  public String getQuestionForm(@RequestParam Long raceId, Model model) {
    var raceExists = raceService.getQuestionsDtoById(raceId);
    raceExists.orElseThrow(() -> new IllegalArgumentException("Race must exist to add questions!"));

    model.addAttribute("race", raceExists.get());

    return "question/form";
  }

  @PostMapping("/add")
  public String postQuestionForm(RaceQuestionsDto race) {
    raceService.addQuestions(race.getQuestions(), race.getId());

    return "redirect:/home";
  }

  @PostMapping("/{id}/check")
  public String checkAnswer(QuestionAttemptDto question) {
    // TODO: check if the question answer is correct
    // TODO: redirect to according race route

    return entity;
  }

}
