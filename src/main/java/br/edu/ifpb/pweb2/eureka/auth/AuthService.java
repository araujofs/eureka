package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService service;

  public AuthResponse authenticate(AuthRequest auth) {
    var user = service.getByName(auth.name()).orElse(null);

    return new AuthResponse(user.getId(), user.getName(), user.isAdmin());
  }

}
