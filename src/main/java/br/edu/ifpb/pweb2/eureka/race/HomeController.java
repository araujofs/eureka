package br.edu.ifpb.pweb2.eureka.race;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

  private final RaceService service;
  public static final String ERROR_MESSAGE_MODEL_ATTR = "errorMessage";

  @GetMapping("/home")
  public String getHome(Model model) {
    var races = service.getAll();
    model.addAttribute("races", races);

    return "home";
  }

  @GetMapping
  public String getRoot() {
    return "redirect:/home";
  }
}
