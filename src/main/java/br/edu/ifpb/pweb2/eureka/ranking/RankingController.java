package br.edu.ifpb.pweb2.eureka.ranking;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.eureka.config.security.CustomUserDetails;
import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.race.RaceService;
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
  public String getRankingPage(Model model, Authentication auth) {
    long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();

    var ranking = service.getRanking(userId);
    model.addAttribute("ranking", ranking);

    return "ranking/index";
  }

  @GetMapping("/race/{id}")
  public String getRaceRankingPage(@PathVariable Long id, Model model, Authentication auth) {
    long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
    Race race = raceService.getByIdWithResults(id).orElseThrow(() -> new IllegalArgumentException("Corrida não existe!"));

    var ranking = service.getRanking(userId, race);
    model.addAttribute("ranking", ranking);
    model.addAttribute("race", race);

    return "ranking/index";
  }
}
