package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.user.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserService service;

  public AuthResponse authenticate(AuthRequest auth) {
    System.out.println(auth.name());
    var user = service.getByName(auth.name()).orElse(null);

    if (user == null) {
      user = service.create(auth.name());
    }

    return new AuthResponse(user.getId(), user.getName(), user.isAdmin());
  }

}
