package br.edu.ifpb.pweb2.eureka.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repo;

  public Optional<User> getById(Long id)  {
    return repo.findById(id);
  }

  public List<User> getAllWithResults()  {
    return repo.findAllByAdminFalseOrderByNameAsc();
  }

  public Optional<User> getByName(String name)  {
    return repo.findByName(name);
  }

  public User create(String name) {
    var user = new User();
    user.setAdmin(false);
    user.setName(name);

    return repo.save(user);
  }

  public User edit(User u) {
    return repo.save(u);
  }
    
}
