package br.edu.ifpb.pweb2.eureka.race;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

  private final RaceService service;
  public static final String ERROR_MESSAGE_MODEL_ATTR = "errorMessage";

  @GetMapping("/home")
  public String getHome(Model model, HttpSession session) {
    var admin = (Boolean) session.getAttribute("admin");
    var userId = (Long) session.getAttribute("userId");
    var races = (admin != null && admin) ? service.getAll() : service.getAllActive(userId);

    System.out.println("Races: " + races);

    model.addAttribute("races", races);

    return "home";
  }

  @GetMapping
  public String getRoot() {
    return "redirect:/home";
  }
}
