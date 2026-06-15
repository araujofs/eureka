package br.edu.ifpb.pweb2.eureka;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.edu.ifpb.pweb2.eureka.question.Question;
import br.edu.ifpb.pweb2.eureka.question.attempt.AnswerAttempt;
import br.edu.ifpb.pweb2.eureka.question.difficulty.Difficulty;
import br.edu.ifpb.pweb2.eureka.race.Race;
import br.edu.ifpb.pweb2.eureka.race.RaceRepository;
import br.edu.ifpb.pweb2.eureka.result.Result;
import br.edu.ifpb.pweb2.eureka.result.ResultRepository;
import br.edu.ifpb.pweb2.eureka.user.User;
import br.edu.ifpb.pweb2.eureka.user.UserRepository;

@SpringBootApplication
public class EurekaApplication {
  private static final String DEFAULT_SEEDED_PASSWORD = "123456";
  private static final String SEED_MARKER_RACE_TITLE = "Arte clássica";

  public static void main(String[] args) {
    SpringApplication.run(EurekaApplication.class, args);
  }

  @Bean
  CommandLineRunner initAdminUser(
      UserRepository userRepo,
      RaceRepository raceRepo,
      ResultRepository resultRepo,
      PasswordEncoder passwordEncoder) {
    var adminName = "nilce";
    var userName = "arthur";

    return args -> {
      if (isSeedAlreadyDone(userRepo, raceRepo, adminName, userName)) {
        return;
      }

      seedUsers(userRepo, passwordEncoder, adminName, userName);

      var race1 = new Race();
      race1.setTitle(SEED_MARKER_RACE_TITLE);
      race1.setDescription("Pintura, escultura e movimentos artísticos.");
      race1.setDuration(150);
      race1.setActive(true);
      race1.addQuestion(question(
          "Quem pintou a Mona Lisa?",
          Difficulty.EASY,
          List.of("Michelangelo", "Leonardo da Vinci", "Rafael", "Botticelli"),
          1));
      race1.addQuestion(question(
          "Qual movimento é associado a Picasso?",
          Difficulty.MEDIUM,
          List.of("Barroco", "Surrealismo", "Cubismo", "Impressionismo"),
          2));
      race1.addQuestion(question(
          "O que define a arte barroca?",
          Difficulty.MEDIUM,
          List.of("Minimalismo", "Geometria pura", "Cores planas", "Contraste de luz e sombra"),
          3));
      race1.addQuestion(question(
          "Qual cidade abriga o Louvre?",
          Difficulty.EASY,
          List.of("Roma", "Paris", "Londres", "Madri"),
          1));
      race1.addQuestion(question(
          "O termo Renascimento se refere a qual período?",
          Difficulty.HARD,
          List.of("Século XII", "Séculos XV e XVI", "Século XVIII", "Século XX"),
          1));
      race1.addQuestion(question(
          "Qual escultura é de Michelangelo?",
          Difficulty.MEDIUM,
          List.of("Davi", "Vênus de Milo", "O Pensador", "Vitória de Samotrácia"),
          0));
      race1.addQuestion(question(
          "Qual artista é conhecido por pintar o teto da Capela Sistina?",
          Difficulty.EASY,
          List.of("Rafael", "Michelangelo", "Caravaggio", "Tintoretto"),
          1));
      raceRepo.save(race1);

      var race2 = new Race();
      race2.setTitle("Política e cidadania");
      race2.setDescription("Instituições, direitos e organização do Estado.");
      race2.setDuration(165);
      race2.setActive(true);
      race2.addQuestion(question(
          "Qual poder cria as leis?",
          Difficulty.EASY,
          List.of("Executivo", "Judiciário", "Legislativo", "Moderador"),
          2));
      race2.addQuestion(question(
          "O que é uma constituição?",
          Difficulty.MEDIUM,
          List.of("Partido político", "Conjunto de normas fundamentais", "Lei municipal", "Plano de governo"),
          1));
      race2.addQuestion(question(
          "Qual é a função do voto?",
          Difficulty.EASY,
          List.of("Aplicar multas", "Criar impostos", "Escolher representantes", "Julgar crimes"),
          2));
      race2.addQuestion(question(
          "Separação de poderes foi proposta por qual autor?",
          Difficulty.HARD,
          List.of("Hobbes", "Locke", "Rousseau", "Montesquieu"),
          3));
      race2.addQuestion(question(
          "O que significa Estado laico?",
          Difficulty.MEDIUM,
          List.of("Com voto censitário", "Sem religião oficial", "Com monarquia", "Governado por sacerdotes"),
          1));
      race2.addQuestion(question(
          "O que é sufrágio universal?",
          Difficulty.MEDIUM,
          List.of("Direito de votar sem restrições", "Voto secreto obrigatório", "Voto apenas feminino", "Voto por procuração"),
          0));
      race2.addQuestion(question(
          "Qual é a função do Judiciário?",
          Difficulty.EASY,
          List.of("Fiscalizar eleições", "Aplicar e interpretar as leis", "Criar leis", "Administrar o país"),
          1));
      raceRepo.save(race2);

      var race3 = new Race();
      race3.setTitle("Tecnologia e inovação");
      race3.setDescription("Computação, redes e impactos tecnológicos.");
      race3.setDuration(180);
      race3.setActive(true);
      race3.addQuestion(question(
          "O que significa CPU?",
          Difficulty.EASY,
          List.of("Core Program Utility", "Central Processing Unit", "Control Power Unit", "Central Protocol Unit"),
          1));
      race3.addQuestion(question(
          "Qual protocolo é usado para páginas web?",
          Difficulty.EASY,
          List.of("FTP", "SMTP", "HTTP", "SSH"),
          2));
      race3.addQuestion(question(
          "Qual linguagem é conhecida por rodar na JVM?",
          Difficulty.MEDIUM,
          List.of("Go", "Rust", "Java", "C"),
          2));
      race3.addQuestion(question(
          "O que é criptografia?",
          Difficulty.MEDIUM,
          List.of("Método de compactação", "Técnica de proteger dados", "Tipo de rede", "Modelo de CPU"),
          1));
      race3.addQuestion(question(
          "Qual conceito descreve infraestrutura sob demanda na nuvem?",
          Difficulty.HARD,
          List.of("Edge isolado", "Mainframe local", "Computação em nuvem", "Batch offline"),
          2));
      race3.addQuestion(question(
          "O que é um banco de dados relacional?",
          Difficulty.MEDIUM,
          List.of("Armazena dados em tabelas", "Armazena dados em grafos", "Armazena dados em arquivos binários", "Armazena dados em filas"),
          0));
      race3.addQuestion(question(
          "Qual prática protege contas com um fator extra?",
          Difficulty.EASY,
          List.of("Autenticação de dois fatores", "Cache local", "Minificação", "Balanceamento manual"),
          0));
      raceRepo.save(race3);

      var race4 = new Race();
      race4.setTitle("Esportes e regras");
      race4.setDescription("Modalidades, história e regulamentos.");
      race4.setDuration(140);
      race4.setActive(true);
      race4.addQuestion(question(
          "Quantos jogadores tem um time de futebol em campo?",
          Difficulty.EASY,
          List.of("10", "11", "9", "12"),
          1));
      race4.addQuestion(question(
          "Qual país sediou a Copa do Mundo de 2014?",
          Difficulty.EASY,
          List.of("Alemanha", "Brasil", "Espanha", "Rússia"),
          1));
      race4.addQuestion(question(
          "Em qual esporte se usa o termo home run?",
          Difficulty.MEDIUM,
          List.of("Basquete", "Beisebol", "Tênis", "Natação"),
          1));
      race4.addQuestion(question(
          "Quantos pontos vale uma cesta de 3 no basquete?",
          Difficulty.EASY,
          List.of("2", "4", "3", "1"),
          2));
      race4.addQuestion(question(
          "Qual é a duração oficial de uma maratona?",
          Difficulty.HARD,
          List.of("35 km", "50 km", "40 km", "42,195 km"),
          3));
      race4.addQuestion(question(
          "Quantos sets são necessários para vencer no tênis masculino em Grand Slams?",
          Difficulty.MEDIUM,
          List.of("2", "3", "4", "5"),
          1));
      race4.addQuestion(question(
          "Qual esporte usa o termo touchdown?",
          Difficulty.EASY,
          List.of("Futebol americano", "Rúgbi", "Futebol", "Hóquei"),
          0));
      raceRepo.save(race4);

      var race5 = new Race();
      race5.setTitle("História geral");
      race5.setDescription("Fatos e períodos históricos.");
      race5.setDuration(180);
      race5.setActive(true);
      race5.addQuestion(question(
          "Em que ano terminou a Segunda Guerra Mundial?",
          Difficulty.MEDIUM,
          List.of("1918", "1950", "1939", "1945"),
          3));
      race5.addQuestion(question(
          "Quem foi o primeiro presidente do Brasil?",
          Difficulty.MEDIUM,
          List.of("Floriano Peixoto", "Deodoro da Fonseca", "Getúlio Vargas", "JK"),
          1));
      race5.addQuestion(question(
          "A Revolução Francesa começou em qual ano?",
          Difficulty.HARD,
          List.of("1700", "1776", "1789", "1812"),
          2));
      race5.addQuestion(question(
          "Qual império construiu o Coliseu?",
          Difficulty.EASY,
          List.of("Grego", "Persa", "Romano", "Egípcio"),
          2));
      race5.addQuestion(question(
          "O que foi a Rota da Seda?",
          Difficulty.MEDIUM,
          List.of("Rota marítima", "Rede de comércio entre Ásia e Europa", "Conflito militar", "Tratado de paz"),
          1));
      race5.addQuestion(question(
          "Em que ano ocorreu a queda do Muro de Berlim?",
          Difficulty.MEDIUM,
          List.of("1989", "1991", "1979", "1961"),
          0));
      race5.addQuestion(question(
          "Quem liderou a independência da Índia?",
          Difficulty.EASY,
          List.of("Mahatma Gandhi", "Nehru", "Churchill", "Mandela"),
          0));
      raceRepo.save(race5);

      var race6 = new Race();
      race6.setTitle("Ciência e natureza");
      race6.setDescription("Física, biologia e astronomia.");
      race6.setDuration(160);
      race6.setActive(true);
      race6.addQuestion(question(
          "Qual planeta é conhecido como planeta vermelho?",
          Difficulty.EASY,
          List.of("Saturno", "Marte", "Vênus", "Júpiter"),
          1));
      race6.addQuestion(question(
          "Qual é a unidade básica da vida?",
          Difficulty.EASY,
          List.of("Molécula", "Átomo", "Célula", "Tecido"),
          2));
      race6.addQuestion(question(
          "Quem formulou as leis do movimento?",
          Difficulty.MEDIUM,
          List.of("Galileu", "Newton", "Kepler", "Einstein"),
          1));
      race6.addQuestion(question(
          "O que mede a escala Richter?",
          Difficulty.MEDIUM,
          List.of("Pressão atmosférica", "Velocidade do vento", "Magnitude de terremotos", "Temperatura"),
          2));
      race6.addQuestion(question(
          "Qual gás é essencial para a respiração humana?",
          Difficulty.EASY,
          List.of("Nitrogênio", "Hidrogênio", "Oxigênio", "Hélio"),
          2));
      race6.addQuestion(question(
          "Qual órgão é responsável por bombear o sangue?",
          Difficulty.EASY,
          List.of("Coração", "Pulmão", "Fígado", "Rim"),
          0));
      race6.addQuestion(question(
          "Qual é a estrela mais próxima da Terra?",
          Difficulty.MEDIUM,
          List.of("Sol", "Sirius", "Alpha Centauri", "Betelgeuse"),
          0));
      raceRepo.save(race6);

      var race7 = new Race();
      race7.setTitle("Cinema e música");
      race7.setDescription("Filmes, trilhas e artistas.");
      race7.setDuration(150);
      race7.setActive(true);
      race7.addQuestion(question(
          "Qual filme venceu o Oscar de 1994?",
          Difficulty.HARD,
          List.of("Pulp Fiction", "Um Sonho de Liberdade", "Forrest Gump", "O Rei Leão"),
          2));
      race7.addQuestion(question(
          "Quem é conhecido como o rei do pop?",
          Difficulty.EASY,
          List.of("Elvis Presley", "Michael Jackson", "Prince", "Freddie Mercury"),
          1));
      race7.addQuestion(question(
          "Qual instrumento tem teclas e pedais?",
          Difficulty.EASY,
          List.of("Violino", "Flauta", "Trompete", "Piano"),
          3));
      race7.addQuestion(question(
          "Qual diretor é associado a filmes como Inception?",
          Difficulty.MEDIUM,
          List.of("James Cameron", "Ridley Scott", "Christopher Nolan", "Steven Spielberg"),
          2));
      race7.addQuestion(question(
          "Qual gênero musical surgiu no Bronx nos anos 70?",
          Difficulty.MEDIUM,
          List.of("Samba", "Jazz", "Hip hop", "Forró"),
          2));
      race7.addQuestion(question(
          "Qual filme é dirigido por Steven Spielberg?",
          Difficulty.MEDIUM,
          List.of("Jurassic Park", "O Poderoso Chefão", "Clube da Luta", "Pulp Fiction"),
          0));
      race7.addQuestion(question(
          "Qual instrumento é de sopro?",
          Difficulty.EASY,
          List.of("Trompete", "Violoncelo", "Guitarra", "Piano"),
          0));
      raceRepo.save(race7);

      var race8 = new Race();
      race8.setTitle("Geografia e mapas");
      race8.setDescription("Países, capitais e acidentes geográficos.");
      race8.setDuration(150);
      race8.setActive(true);
      race8.addQuestion(question(
          "Qual é a capital do Canadá?",
          Difficulty.MEDIUM,
          List.of("Vancouver", "Ottawa", "Toronto", "Montreal"),
          1));
      race8.addQuestion(question(
          "Qual é o maior oceano do planeta?",
          Difficulty.EASY,
          List.of("Índico", "Atlântico", "Pacífico", "Ártico"),
          2));
      race8.addQuestion(question(
          "Em qual continente fica o Deserto do Saara?",
          Difficulty.EASY,
          List.of("Europa", "América", "Ásia", "África"),
          3));
      race8.addQuestion(question(
          "Qual é o rio mais extenso do mundo?",
          Difficulty.HARD,
          List.of("Nilo", "Yangtze", "Amazonas", "Mississippi"),
          2));
      race8.addQuestion(question(
          "Qual país tem maior área territorial?",
          Difficulty.MEDIUM,
          List.of("Canadá", "China", "Rússia", "Brasil"),
          2));
      race8.addQuestion(question(
          "Qual é a capital da Austrália?",
          Difficulty.MEDIUM,
          List.of("Sydney", "Melbourne", "Canberra", "Perth"),
          2));
      race8.addQuestion(question(
          "Qual é o maior país da América do Sul?",
          Difficulty.EASY,
          List.of("Brasil", "Argentina", "Colômbia", "Peru"),
          0));
      raceRepo.save(race8);

      var race9 = new Race();
      race9.setTitle("Literatura e língua");
      race9.setDescription("Autores, obras e conceitos.");
      race9.setDuration(170);
      race9.setActive(true);
      race9.addQuestion(question(
          "Quem escreveu Dom Casmurro?",
          Difficulty.MEDIUM,
          List.of("José de Alencar", "Machado de Assis", "Clarice Lispector", "Graciliano Ramos"),
          1));
      race9.addQuestion(question(
          "O que é um haicai?",
          Difficulty.HARD,
          List.of("Peça teatral", "Ensaio filosófico", "Poema curto de origem japonesa", "Romance histórico"),
          2));
      race9.addQuestion(question(
          "Qual é o gênero de Os Lusíadas?",
          Difficulty.MEDIUM,
          List.of("Crônica", "Epopeia", "Conto", "Novela"),
          1));
      race9.addQuestion(question(
          "Quem escreveu Romeu e Julieta?",
          Difficulty.EASY,
          List.of("Charles Dickens", "Goethe", "William Shakespeare", "Jane Austen"),
          2));
      race9.addQuestion(question(
          "Qual figura de linguagem compara sem usar como?",
          Difficulty.HARD,
          List.of("Metonímia", "Hipérbole", "Metáfora", "Eufemismo"),
          2));
      race9.addQuestion(question(
          "Quem escreveu O Pequeno Príncipe?",
          Difficulty.EASY,
          List.of("Antoine de Saint-Exupéry", "Victor Hugo", "Júlio Verne", "Albert Camus"),
          0));
      race9.addQuestion(question(
          "Qual é o plural de cidadão?",
          Difficulty.EASY,
          List.of("Cidadãos", "Cidadães", "Cidadões", "Cidadõeses"),
          0));
      raceRepo.save(race9);

      var race10 = new Race();
      race10.setTitle("Economia e sociedade");
      race10.setDescription("Conceitos básicos de economia e consumo.");
      race10.setDuration(170);
      race10.setActive(true);
      race10.addQuestion(question(
          "O que significa inflação?",
          Difficulty.EASY,
          List.of("Queda do PIB", "Redução de impostos", "Aumento geral de preços", "Alta do desemprego"),
          2));
      race10.addQuestion(question(
          "O que é oferta e demanda?",
          Difficulty.MEDIUM,
          List.of("Sistema bancário", "Relação entre quantidade e preço", "Política monetária", "Crédito fiscal"),
          1));
      race10.addQuestion(question(
          "PIB é a sigla de que?",
          Difficulty.EASY,
          List.of("Produto Industrial Básico", "Preço Internacional Básico", "Produto Interno Bruto", "Plano de Investimento Base"),
          2));
      race10.addQuestion(question(
          "O que é salário mínimo?",
          Difficulty.EASY,
          List.of("Salário de estagiário", "Menor valor legal para salário", "Maior salário possível", "Salário de aposentado"),
          1));
      race10.addQuestion(question(
          "O que é política fiscal?",
          Difficulty.HARD,
          List.of("Criação de moeda", "Uso de gastos e impostos pelo governo", "Controle da taxa de juros", "Privatização de bancos"),
          1));
      race10.addQuestion(question(
          "O que significa taxa de juros?",
          Difficulty.MEDIUM,
          List.of("Custo do dinheiro no tempo", "Valor do salário mínimo", "Preço do combustível", "Custo do frete"),
          0));
      race10.addQuestion(question(
          "Qual é o objetivo de um orçamento público?",
          Difficulty.MEDIUM,
          List.of("Planejar receitas e despesas", "Aumentar a inflação", "Reduzir a produção", "Eliminar impostos"),
          0));

      var race11 = new Race();
      race11.setTitle("Gastronomia e sabores");
      race11.setDescription("Ingredientes, pratos e técnicas culinárias.");
      race11.setDuration(150);
      race11.setActive(true);
      race11.addQuestion(question(
          "Qual ingrediente é base do guacamole?",
          Difficulty.EASY,
          List.of("Abacate", "Tomate", "Pepino", "Batata"),
          0));
      race11.addQuestion(question(
          "Qual técnica cozinha alimentos no vapor?",
          Difficulty.MEDIUM,
          List.of("Vapor", "Grelhar", "Fritar", "Assar"),
          0));
      race11.addQuestion(question(
          "Qual país é famoso pelo sushi?",
          Difficulty.EASY,
          List.of("Japão", "Itália", "México", "Índia"),
          0));
      race11.addQuestion(question(
          "O que é al dente?",
          Difficulty.MEDIUM,
          List.of("Massa cozida firme", "Massa muito mole", "Massa crua", "Massa frita"),
          0));
      race11.addQuestion(question(
          "Qual queijo é usado no risoto tradicional?",
          Difficulty.HARD,
          List.of("Parmesão", "Cheddar", "Mussarela", "Gorgonzola"),
          0));
      raceRepo.save(race11);

      var race12 = new Race();
      race12.setTitle("Meio ambiente");
      race12.setDescription("Sustentabilidade, clima e preservação.");
      race12.setDuration(150);
      race12.setActive(true);
      race12.addQuestion(question(
          "O que é energia renovável?",
          Difficulty.MEDIUM,
          List.of("Energia de fontes que se renovam", "Energia nuclear", "Energia de carvão", "Energia fóssil"),
          0));
      race12.addQuestion(question(
          "Qual gás é um dos principais do efeito estufa?",
          Difficulty.MEDIUM,
          List.of("Dióxido de carbono", "Oxigênio", "Hélio", "Argônio"),
          0));
      race12.addQuestion(question(
          "O que significa reciclar?",
          Difficulty.EASY,
          List.of("Reaproveitar materiais", "Queimar resíduos", "Enterrar lixo", "Misturar resíduos"),
          0));
      race12.addQuestion(question(
          "Qual bioma brasileiro é conhecido como savana?",
          Difficulty.EASY,
          List.of("Cerrado", "Caatinga", "Amazônia", "Pampa"),
          0));
      race12.addQuestion(question(
          "Qual atitude reduz o desperdício de água?",
          Difficulty.MEDIUM,
          List.of("Consertar vazamentos", "Banho prolongado", "Lavar calçada com mangueira", "Deixar torneira aberta"),
          0));
      raceRepo.save(race12);

      var race13 = new Race();
      race13.setTitle("Curiosidades gerais");
      race13.setDescription("Fatos variados e cultura geral.");
      race13.setDuration(150);
      race13.setActive(true);
      race13.addQuestion(question(
          "Quantos continentes existem?",
          Difficulty.EASY,
          List.of("5", "6", "7", "8"),
          2));
      race13.addQuestion(question(
          "Qual é o maior mamífero do mundo?",
          Difficulty.EASY,
          List.of("Baleia-azul", "Elefante", "Rinoceronte", "Girafa"),
          0));
      race13.addQuestion(question(
          "Qual é a capital de Portugal?",
          Difficulty.EASY,
          List.of("Lisboa", "Porto", "Coimbra", "Braga"),
          0));
      race13.addQuestion(question(
          "Quantos lados tem um hexágono?",
          Difficulty.MEDIUM,
          List.of("5", "6", "7", "8"),
          1));
      race13.addQuestion(question(
          "Qual instrumento mede a temperatura?",
          Difficulty.EASY,
          List.of("Termômetro", "Barômetro", "Anemômetro", "Higrômetro"),
          0));
      raceRepo.save(race13);
      raceRepo.save(race10);

      seedRanking(resultRepo, userRepo, passwordEncoder, List.of(
          race1,
          race2,
          race3,
          race4,
          race5,
          race6,
          race7,
          race8,
          race9,
          race10,
          race11,
          race12,
          race13
      ));

    };
  }

  private static Question question(
      String statement,
      Difficulty difficulty,
      List<String> answers,
      int correctAnswer) {
    var question = new Question();
    question.setStatement(statement);
    question.setDifficulty(difficulty);
    question.setAnswers(answers);
    question.setCorrectAnswer(correctAnswer);
    return question;
  }

  private static boolean isSeedAlreadyDone(
      UserRepository userRepo,
      RaceRepository raceRepo,
      String adminName,
      String userName) {
    return raceRepo.existsByTitle(SEED_MARKER_RACE_TITLE)
        && userRepo.existsByName(adminName)
        && userRepo.existsByName(userName);
  }

  private static void seedUsers(
      UserRepository userRepo,
      PasswordEncoder passwordEncoder,
      String adminName,
      String userName) {
    seedUser(userRepo, passwordEncoder, adminName, true);
    seedUser(userRepo, passwordEncoder, userName, false);
  }

  private static User seedUser(
      UserRepository userRepo,
      PasswordEncoder passwordEncoder,
      String name,
      boolean admin) {
    var user = userRepo.findByName(name).orElseGet(() -> {
      var newUser = new User();
      newUser.setAdmin(admin);
      newUser.setName(name);
      return newUser;
    });

    if (user.getPassword() == null || user.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(DEFAULT_SEEDED_PASSWORD));
    }

    return userRepo.save(user);
  }

  private static void seedRanking(
      ResultRepository resultRepo,
      UserRepository userRepo,
      PasswordEncoder passwordEncoder,
      List<Race> races) {
    var userNames = List.of(
        "joao",
        "maria",
        "ana",
        "paulo",
        "lucas",
        "julia",
        "rafael",
        "mariana",
        "gabriel",
        "beatriz",
        "fernando",
        "camila",
        "vitor",
        "isabela",
        "bruno",
        "leticia",
        "gustavo",
        "amanda",
        "rodrigo",
        "larissa",
        "thiago",
        "patricia"
    );

    var users = new ArrayList<User>();
    for (var name : userNames) {
      users.add(seedUser(userRepo, passwordEncoder, name, false));
    }

    if (users.isEmpty() || races.isEmpty()) {
      return;
    }

    for (int i = 0; i < users.size(); i++) {
      var user = users.get(i);
      var race = races.get(i % races.size());

      if (resultRepo.findByParticipantAndRace(user, race).isPresent()) {
        continue;
      }

      var result = new Result();
      result.setParticipant(user);
      result.setRace(race);
      var baseTime = LocalDateTime.now().minusDays(1 + (i % 20));
      result.setStartedRaceAt(baseTime.minusMinutes(5));
      result.setFinishedRaceAt(baseTime.minusMinutes(4));

      var questions = race.getQuestions();
      var maxAnswers = Math.min(5, questions.size());
      for (int q = 0; q < maxAnswers; q++) {
        var question = questions.get(q);
        int answerIndex = (i + q) % question.getAnswers().size();

        var answerAttempt = new AnswerAttempt();
        answerAttempt.setQuestion(question);
        answerAttempt.setAnswerIndex(answerIndex);
        answerAttempt.setAnswerCorrect(answerIndex == question.getCorrectAnswer());
        result.addAnswer(answerAttempt);
      }

      resultRepo.save(result);
    }
  }

}
