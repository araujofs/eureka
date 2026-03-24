package br.edu.ifpb.pweb2.eureka;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;

@SpringBootApplication
public class EurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
  }

  @Bean
  CommandLineRunner initAdminUser(UserRepository userRepo) {
    var name = "nilce";

    return args -> {
      if (!userRepo.existsByName(name)) {
        var admin = new User();

        admin.setAdmin(true);
        admin.setName(name);

        userRepo.save(admin);
      }
    };
  }

}
