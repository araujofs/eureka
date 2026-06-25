package br.edu.ifpb.pweb2.eureka.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.auth.AuthRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public Optional<User> getById(Long id)  {
    return repo.findById(id);
  }

  public List<User> getAllWithResults()  {
    return repo.findAllByAdminFalseOrderByNameAsc();
  }

  public Optional<User> getByName(String name)  {
    return repo.findByName(name);
  }

  public User create(AuthRequest data) {
    if (repo.findByName(data.name()).isPresent()) {
      throw new IllegalArgumentException("Esse nome de usuário já está em uso!");
    }

    var user = new User();
    user.setAdmin(false);
    user.setName(data.name());
    user.setPassword(encoder.encode(data.password()));

    return repo.save(user);
  }

  public User edit(User u) {
    return repo.save(u);
  }
    
}
