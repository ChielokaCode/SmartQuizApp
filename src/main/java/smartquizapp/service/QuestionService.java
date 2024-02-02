package smartquizapp.service;

import smartquizapp.dto.QuestionRequestDto;

public interface QuestionService {
    void editQuestion(Long id, QuestionRequestDto questionRequestDto);

}
