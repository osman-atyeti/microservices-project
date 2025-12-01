package com.osman.questionservice.service;

import com.osman.questionservice.model.Question;
import com.osman.questionservice.model.QuestionWrapper;
import com.osman.questionservice.model.Response;
import com.osman.questionservice.repo.QuestionRepo;
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

    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionRepo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        return new ResponseEntity<>(questionRepo.findByCategory(category), HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionRepo.save(question);
        return new ResponseEntity<>("Question added", HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteQuestion(int id) {
        questionRepo.deleteById(id);
        return new ResponseEntity<>("Question deleted", HttpStatus.OK);
    }

    public ResponseEntity<String> updateQuestion(int id, Question updatedQuestion) {
        Optional<Question> optionalQuestion = questionRepo.findById(id);
        if (optionalQuestion.isPresent()) {
            Question existingQuestion = getQuestion(updatedQuestion, optionalQuestion);
            questionRepo.save(existingQuestion);
            return ResponseEntity.ok("Question updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found.");
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

    public ResponseEntity<List<Integer>> getQuestionForService(String category, int numOfQuestions) {

        List<Integer> questions = questionRepo.getRandomQuestionsByCategory(category, numOfQuestions);

        return new ResponseEntity<>(questions, HttpStatus.OK);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
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
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }


    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right = 0;
        for (Response r : responses) {
            Question question = questionRepo.findById(r.getId()).get();
            if (r.getResponse().equals(question.getRightAnswer())) {
                right++;
            }
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }
}
