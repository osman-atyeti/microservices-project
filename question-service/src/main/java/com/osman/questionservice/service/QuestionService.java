package com.osman.questionservice.service;

import com.osman.questionservice.model.Question;
import com.osman.questionservice.model.QuestionWrapper;
import com.osman.questionservice.model.Response;
import com.osman.questionservice.repo.QuestionRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepo questionRepo;

    public QuestionService(QuestionRepo questionRepo) {
        this.questionRepo = questionRepo;
    }

    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return questionRepo.findByCategory(category);
    }

    public String addQuestion(Question question) {
        questionRepo.save(question);
        return "Question added successfully.";
    }

    public String deleteQuestion(int id) {
        questionRepo.deleteById(id);
        return "Question deleted successfully.";
    }

    public String updateQuestion(int id, Question updatedQuestion) {
        Optional<Question> optionalQuestion = questionRepo.findById(id);
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = getQuestion(updatedQuestion, optionalQuestion);
            questionRepo.save(existingQuestion);
            return "Question updated successfully.";
        } else {
            return "Question not found.";
        }
    }

    private static Question getQuestion(Question updatedQuestion, Optional<Question> optionalQuestion) {
        Question existingQuestion = optionalQuestion.get();

        if (updatedQuestion.getQuestionTitle() != null) {
            existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
        }
        if (updatedQuestion.getCategory() != null) {
            existingQuestion.setCategory(updatedQuestion.getCategory());
        }
        if (updatedQuestion.getOption1() != null) {
            existingQuestion.setOption1(updatedQuestion.getOption1());
        }
        if (updatedQuestion.getOption2() != null) {
            existingQuestion.setOption2(updatedQuestion.getOption2());
        }
        if (updatedQuestion.getOption3() != null) {
            existingQuestion.setOption3(updatedQuestion.getOption3());
        }
        if (updatedQuestion.getOption4() != null) {
            existingQuestion.setOption4(updatedQuestion.getOption4());
        }
        if (updatedQuestion.getRightAnswer() != null) {
            existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
        }

        return existingQuestion;
    }

    public List<Integer> getQuestionForService(String category, int numOfQuestions) {


        return questionRepo.getRandomQuestionsByCategory(category, numOfQuestions);

    }

    public List<QuestionWrapper> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Integer questionId : questionIds) {
            questions.add(questionRepo.findById(questionId).get());
        }

        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }
        return wrappers;
    }


    public Integer getScore(List<Response> responses) {
        int right = 0;
        for (Response r : responses) {
            Question question = questionRepo.findById(r.getId()).get();
            if (r.getResponse().equals(question.getRightAnswer())) {
                right++;
            }
        }
        return right;
    }

    public List<Question> getSortedQuestions(String sortBy) {
        return questionRepo.findAll(Sort.by(Sort.Direction.ASC, sortBy));
    }

    public List<Question> getPagedQuestions(int pageNo, int pageSize) {
        Page<Question> all = questionRepo.findAll(PageRequest.of(pageNo, pageSize));
        return all.getContent();
    }
}
