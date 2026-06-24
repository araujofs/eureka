package br.edu.ifpb.pweb2.eureka.question;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class ImageController {

  private final QuestionService service;

  @GetMapping("/{id}/image")
  @ResponseBody
  public ResponseEntity<byte[]> getImageByQuestion(@PathVariable Long id) {
    var question = service.getById(id);

    if (question.isEmpty() || question.get().getImage() == null) {
      return ResponseEntity.notFound().build();
    }

    MediaType type = service.getImageExtension(question.get().getImage());
    if (type == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().contentType(type).body(question.get().getImage().getBytes());
  }
}
