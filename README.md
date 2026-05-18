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
Race
├── id: Long (PK)
├── title: String (unique)
├── description: String
├── duration: Integer
├── active: boolean
├── questions: List<Question> (1:N, cascade ALL, orphanRemoval)
└── results: Set<Result> (1:N, cascade ALL, orphanRemoval)

Question
├── id: Long (PK)
├── statement: String
├── difficulty: Difficulty
├── answers: List<String> (ElementCollection)
├── correctAnswer: Integer
└── race: Race (N:1, FK race_id)

User
├── id: long (PK)
├── name: String (unique)
├── admin: boolean
└── results: Set<Result> (1:N, cascade REMOVE+PERSIST, orphanRemoval)

Result
├── id: Long (PK)
├── participant: User (N:1, FK participant_id)
├── race: Race (N:1, FK race_id)
├── answers: List<AnswerAttempt> (1:N, cascade REMOVE+PERSIST+MERGE, orphanRemoval)
├── startedRaceAt: LocalDateTime (CreationTimestamp)
├── finishedRaceAt: LocalDateTime
└── currentQuestionId: Long
  unique: (participant_id, race_id)

AnswerAttempt
├── id: Long (PK)
├── result: Result (N:1, FK result_id)
├── question: Question (N:1, FK question_id)
├── answerIndex: Integer
└── answerCorrect: boolean
  unique: (result_id, question_id)

Relacionamentos:
Race     ──< Question      (1 race tem N questions)
Race     ──< Result        (1 race tem N results)
User     ──< Result        (1 user tem N results)
Result   ──< AnswerAttempt (1 result tem N attempts)
Question ──< AnswerAttempt (1 question tem N attempts)
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
