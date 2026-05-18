# 🏁 Eureka

Plataforma web para competições de perguntas em formato de **corrida**: cada participante responde questões, acumula pontos por dificuldade e tem seu progresso registrado.

## O que o projeto faz

O Eureka permite:

- criar e gerenciar corridas (`Race`) com título, duração e descrição;
- cadastrar questões (`Question`) por corrida, com alternativas e dificuldade;
- registrar a participação de usuários (`User`) em corridas;
- armazenar resultados (`Result`) por usuário/corrida;
- registrar tentativas de resposta (`AnswerAttempt`) e calcular pontuação com base na dificuldade das questões acertadas.

## Stack principal

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Thymeleaf
- H2 (runtime) + JPA/Hibernate
- Maven
- Tailwind CSS (via pnpm)

## Pré-requisitos

- JDK 21+
- Maven 3.9+

## Como executar

```bash
./mvnw spring-boot:run
```

Após iniciar, a aplicação fica disponível em: http://localhost:8080

## Testes

```bash
./mvnw test
```

## Diagrama textual do modelo (JPA)

```text
[User]
- id
- name (único)
- admin
  1 ────────────────< N
                     [Result]
                     - id
                     - startedRaceAt
                     - finishedRaceAt
                     - currentQuestionId
                     - points (calculado pelas respostas corretas)
                     (único por participant_id + race_id)
  N >─────────────── 1
[Race]
- id
- title (único)
- description
- duration
- active
  1 ────────────────< N [Question]
                     - id
                     - statement
                     - difficulty
                     - answers (ElementCollection)
                     - correctAnswer
                     (único por race_id + statement)

[Result] 1 ─────────< N [AnswerAttempt]
                     - id
                     - answerIndex
                     - answerCorrect
                     (único por result_id + question_id)

[AnswerAttempt] N >── 1 [Question]
```

## Estrutura resumida

```text
src/main/java/br/edu/ifpb/pweb2/eureka
├── race/
├── question/
├── result/
├── user/
└── ...
```
