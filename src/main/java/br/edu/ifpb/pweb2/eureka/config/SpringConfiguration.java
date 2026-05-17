package br.edu.ifpb.pweb2.eureka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.ifpb.pweb2.eureka.auth.AuthInterceptor;
import br.edu.ifpb.pweb2.eureka.race.RaceInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringConfiguration implements WebMvcConfigurer {

  private final AuthInterceptor authInterceptor;
  private final RaceInterceptor raceInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authInterceptor).excludePathPatterns("/css/**", "/auth/**");
    registry.addInterceptor(raceInterceptor).addPathPatterns("/home");
	}
}
