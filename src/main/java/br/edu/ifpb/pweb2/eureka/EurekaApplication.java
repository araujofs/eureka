package br.edu.ifpb.pweb2.eureka;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.question.difficulty.Difficulty;
import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.race.RaceRepository;
import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;

@SpringBootApplication
public class EurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
  }

  @Bean
  CommandLineRunner initAdminUser(UserRepository userRepo, RaceRepository raceRepo) {
    var name = "nilce";

    return args -> {
      if (!userRepo.existsByName(name)) {
        var admin = new User();

        admin.setAdmin(true);
        admin.setName(name);

        userRepo.save(admin);
      }

      for (int i = 0; i < 21; i++) {
        var race = new Race();
        race.setTitle("Test" + (i + 1));
        race.setDescription("Test" + (i + 1));
        race.setDuration(10);
        race.setActive(false);
        raceRepo.save(race);
      }

      var race2 = new Race();
      race2.setTitle("TestX");
      race2.setDescription("TestX");
      race2.setDuration(10);
      race2.setActive(true);

      var newQuestion = new Question();
      newQuestion.setStatement("Question");
      newQuestion.setDifficulty(Difficulty.HARD);
      newQuestion.setAnswers(List.of("Answer1", "Answer2"));
      newQuestion.setCorrectAnswer(1);

      race2.addQuestion(newQuestion);

      raceRepo.save(race2);

    };
  }

}
