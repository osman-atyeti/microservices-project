package com.osman.questionservice.repo;

import com.osman.questionservice.model.Question;
import java.util.List;

public interface QuestionRepositoryCustom {
    List<Question> search(String category, String keyword);
}