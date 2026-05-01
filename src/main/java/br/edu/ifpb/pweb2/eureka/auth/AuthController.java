package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService service;

  @GetMapping
  public String getAuthForm(Model model) {
    if (!model.containsAttribute("authBody")) {
      model.addAttribute("authBody", new AuthRequest(""));
    }

    return "auth/index";
  }

  @PostMapping
  public String postAuthForm(AuthRequest authBody, HttpServletRequest request, RedirectAttributes attributes) {
    var session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    var user = this.service.authenticate(authBody);

    session = request.getSession(true);

    session.setAttribute("userId", user.id());
    session.setAttribute("userName", user.name());
    session.setAttribute("admin", user.admin());

    return "redirect:/home";
  }

  @PostMapping("/logout")
  public String postLogout(HttpServletRequest request) {
    var session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }

    return "redirect:/auth";
  }

}
