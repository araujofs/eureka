package br.edu.ifpb.pweb2.eureka.question;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import br.edu.ifpb.pweb2.eureka.question.dto.QuestionCreateDto;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

  private final QuestionRepository repo;

  public Optional<Question> getById(Long id) {
    return repo.findById(id);
  }

  public Image getImageFromDto(QuestionCreateDto dto) throws IOException, NotFoundException {
    switch (dto.getImageAction()) {
      case ImageAction.KEEP:
        Question question = getById(dto.getId())
            .orElseThrow(() -> new NotFoundException("Question not found with id: " + dto.getId()));

        return question.getImage();

      // case ImageAction.NONE:
      // return null;

      case ImageAction.REPLACE:
        return new Image(
            null,
            dto.getImageUpload().getOriginalFilename(),
            dto.getImageUpload().getBytes());

      // case ImageAction.REMOVE:
      // return null;

      default:
        return null;
    }
  }

  public MediaType getImageExtension(Image i) {
    if (i == null) {
      return null;
    }
    String name = i.getName();

    switch (name.substring(name.lastIndexOf(".") + 1)) {
      case "png":
        return MediaType.IMAGE_PNG;

      case "jpeg", "jpg":
        return MediaType.IMAGE_JPEG;
    
      default:
        return null;
    }
  }
}
