package br.edu.ifpb.pweb2.eureka.config.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository repo;

  @Override
  public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
    User user = repo.findByName(name).orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + name));

    return new CustomUserDetails(user.getName(), user.getPassword(), user.isAdmin());
  }
}
