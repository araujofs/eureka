package br.edu.ifpb.pweb2.eureka.auth;

import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository repo;

  public AuthResponse authenticate(AuthRequest auth) {
    var user = repo.findByName(auth.name()).orElse(null);

    if (user == null) {
      user = createUser(auth);
    }

    return new AuthResponse(user.getName(), user.isAdmin());
  }

  private User createUser(AuthRequest auth) {
    var user = new User();
    user.setAdmin(false);
    user.setName(auth.name());

    return repo.save(user);
  }
}
