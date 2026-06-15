package br.edu.ifpb.pweb2.eureka.config.security;

import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

  private String name;
  private String password;
  private boolean admin;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (admin) {
      return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return List.of(new SimpleGrantedAuthority("ROLE_PARTICIPANT"));
  }

  @Override
  public @Nullable String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

}
