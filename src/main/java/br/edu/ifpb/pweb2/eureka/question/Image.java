package br.edu.ifpb.pweb2.eureka.question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(length = 1024)
  private String name;

  @Column(length = 1024)
  private String url;

  @Column(columnDefinition = "bytea")
  private byte[] bytes;
}
