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
┌─────────────────────────────────┐
│           Participante          │
├─────────────────────────────────┤
│ - id: Long                      │
│ - nome: String                  │
│ - email: String                 │
│ - admin: Boolean                │
│ - corridasFeitas: List          │
└──────────────┬──────────────────┘
               │ corridasFeitas (0..*)
               │
┌──────────────▼──────────────────┐       ┌──────────────────────────────────┐
│             Corrida             │       │             Resultado            │
├─────────────────────────────────┤       ├──────────────────────────────────┤
│ - id: Long                      │◄──────│ - id: Long                       │
│ - titulo: String                │corrida│ - participante: Participante     │
│ - descricao: String             │       │ - corrida: Corrida               │
│ - tempoSegundos: Integer        │       │ - pontuacao: BigDecimal          │
│ - ativa: Boolean                │       │ - dataHora: LocalDateTime        │
│ - perguntas: List               │       └──────────────────────────────────┘
└──────────────┬──────────────────┘
               │ perguntas (0..*)
               │
┌──────────────▼──────────────────┐
│             Pergunta            │
├─────────────────────────────────┤
│ - id: Long                      │
│ - enunciado: String             │
│ - alternativas: List            │
│ - respostaCorreta: Integer      │
│ - corrida: Corrida              │
└─────────────────────────────────┘
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
