package br.edu.ifpb.pweb2.eureka.race;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;

import br.edu.ifpb.pweb2.eureka.result.ResultService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RaceInterceptor implements HandlerInterceptor {

  private final ResultService resultService;

  public void print(Object value) {
    System.out.println(value);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    var session = request.getSession(false);

    if (session == null) {
      return true;
    }

    var resultId = (Long) session.getAttribute(RaceController.RESULT_SESSION_ATTR);

    if (resultId == null) {
      return true;
    }

    var result = resultService.getById(resultId).orElse(null);
    if (result == null) {
      session.removeAttribute(RaceController.ANSWER_SESSION_ATTR);
      session.removeAttribute(RaceController.QUESTIONS_SESSION_ATTR);
      session.removeAttribute(RaceController.RESULT_SESSION_ATTR);
      session.removeAttribute(RaceController.RACE_SESSION_ATTR);

      return true;
    }

    if (LocalDateTime.now().isAfter(result.getStartedRaceAt().plusSeconds(result.getRace().getDuration()))) {
      session.removeAttribute(RaceController.ANSWER_SESSION_ATTR);
      session.removeAttribute(RaceController.QUESTIONS_SESSION_ATTR);
      session.removeAttribute(RaceController.RESULT_SESSION_ATTR);
      session.removeAttribute(RaceController.RACE_SESSION_ATTR);

      result.setFinishedRaceAt(result.getStartedRaceAt().plusMinutes(result.getRace().getDuration()));
      resultService.edit(result);

      return true;
    }

    response.sendRedirect("/race/" + result.getRace().getId() + "/running");
    return false;
  }

}
