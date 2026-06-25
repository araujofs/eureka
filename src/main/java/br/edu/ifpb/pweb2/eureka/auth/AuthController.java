package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.ifpb.pweb2.eureka.user.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService service;

  @GetMapping
  public String getAuthForm(Model model, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated())
      return "redirect:/home";

    if (!model.containsAttribute("authBody")) {
      model.addAttribute("authBody", new AuthRequest("", ""));
    }

    return "auth/index";
  }

  @GetMapping("/new")
  public String getSignupPage(Model model, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated())
      return "redirect:/home";

    if (!model.containsAttribute("authBody")) {
      model.addAttribute("authBody", new AuthRequest("", ""));
    }

    return "auth/new";
  }

  @PostMapping("/new")
  public String postSignup(AuthRequest data, Model model) {
    try {
      service.create(data);
    } catch (Exception e) {
      model.addAttribute("authBody", new AuthRequest(data.name(), ""));
      model.addAttribute("errorMessage", e.getMessage());
      return "auth/new";
    }

    return "redirect:/auth";
  }

  // @PostMapping
  // public String postAuthForm(AuthRequest authBody, HttpServletRequest request,
  // RedirectAttributes attributes) {
  // var session = request.getSession(false);
  // if (session != null) {
  // session.invalidate();
  // }
  //
  // var user = this.service.authenticate(authBody);
  //
  // session = request.getSession(true);
  //
  // session.setAttribute("userId", user.id());
  // session.setAttribute("userName", user.name());
  // session.setAttribute("admin", user.admin());
  //
  // return "redirect:/home";
  // }

  // @PostMapping("/logout")
  // public String postLogout(HttpServletRequest request) {
  // var session = request.getSession(false);
  // if (session != null) {
  // session.invalidate();
  // }
  //
  // return "redirect:/auth";
  // }

}
