package br.edu.ifpb.pweb2.eureka.ranking;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.race.RaceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

  private final RankingService service;
  private final RaceService raceService;

  @GetMapping
  public String getRankingPage(Model model, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");

    var ranking = service.getRanking(userId);
    model.addAttribute("ranking", ranking);

    return "ranking/index";
  }

  @GetMapping("/race/{id}")
  public String getRaceRankingPage(@PathVariable Long id, Model model, HttpSession session) {
    Long userId = (Long) session.getAttribute("userId");
    Race race = raceService.getByIdWithResults(id).orElseThrow(() -> new IllegalArgumentException("Corrida não existe!"));

    var ranking = service.getRanking(userId, race);
    model.addAttribute("ranking", ranking);
    model.addAttribute("race", race);

    return "ranking/index";
  }
}
