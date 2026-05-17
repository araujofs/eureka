package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    var session = request.getSession(false);

    if (session == null) {
      response.sendRedirect("/auth");
      return false;
    }

    if (session.getAttribute("userName") == null) {
      session.invalidate();
      response.sendRedirect("/auth");
      return false;
    }

    return true;
  }

}
