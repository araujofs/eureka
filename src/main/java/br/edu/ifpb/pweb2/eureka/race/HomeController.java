package br.edu.ifpb.pweb2.eureka.race;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.eureka.config.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

  private final RaceService service;
  public static final String ERROR_MESSAGE_MODEL_ATTR = "errorMessage";

  @GetMapping("/home")
  public String getHome(Model model, HttpSession session, Authentication auth) {
    boolean admin = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

    if (admin)
      return "redirect:/admin";

    long userId = ((CustomUserDetails) auth.getPrincipal()).getUserId();
    var races = service.getAllActive(userId);

    model.addAttribute("races", races);

    return "home";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public String getAdminHome(Model model, HttpSession session, Authentication auth) {
    var races = service.getAll();

    model.addAttribute("races", races);

    return "home-admin";
  }

  @GetMapping
  public String getRoot() {
    return "redirect:/home";
  }
}
