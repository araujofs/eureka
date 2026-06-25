package br.edu.ifpb.pweb2.eureka.question;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.pweb2.eureka.race.RaceService;
import br.edu.ifpb.pweb2.eureka.race.dto.RaceQuestionsDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final RaceService raceService;
  private final QuestionService service;

  @GetMapping("/add")
  public String getQuestionForm(@RequestParam Long raceId, Model model) {
    var raceExists = raceService.getQuestionsDtoById(raceId);
    raceExists.orElseThrow(() -> new IllegalArgumentException("Corrida não existe!"));

    model.addAttribute("race", raceExists.get());

    return "question/form";
  }

  @PostMapping("/add")
  public String postQuestionForm(RaceQuestionsDto race, HttpServletRequest request) {
    raceService.addQuestions(race.getQuestions(), race.getId());

    return "redirect:/home";
  }
}
