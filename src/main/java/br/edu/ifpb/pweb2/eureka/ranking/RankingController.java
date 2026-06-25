package br.edu.ifpb.pweb2.eureka.ranking;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.ifpb.pweb2.eureka.config.security.CustomUserDetails;
import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.race.RaceService;
import br.edu.ifpb.pweb2.eureka.ranking.dto.RankDto;
import br.edu.ifpb.pweb2.eureka.result.Result;
import br.edu.ifpb.pweb2.eureka.user.User;
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
  public String getRankingPage(Model model, Authentication auth, @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "3") int size) {
    long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();

    Pageable paging = PageRequest.of(page - 1, size, Sort.by("totalPoints").descending());

    RankDto<User> ranking = service.getOverallRanking(userId, paging);
    model.addAttribute("ranking", ranking);
    model.addAttribute("paging", paging);
    model.addAttribute("page", ranking.getPage());

    return "ranking/index";
  }

  @GetMapping("/race/{id}")
  public String getRaceRankingPage(@PathVariable Long id, Model model, Authentication auth,
      @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
    long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
    Race race = raceService.getByIdWithResults(id)
        .orElseThrow(() -> new IllegalArgumentException("Corrida não existe!"));

    Pageable paging = PageRequest.of(page - 1, size, Sort.by("totalPoints").descending());

    RankDto<Result> ranking = service.getRankingByRace(race, userId, paging);
    model.addAttribute("ranking", ranking);
    model.addAttribute("race", race);
    model.addAttribute("paging", paging);
    model.addAttribute("page", ranking.getPage());

    return "ranking/index";
  }
}
