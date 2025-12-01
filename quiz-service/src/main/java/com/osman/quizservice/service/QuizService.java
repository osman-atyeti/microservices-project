package com.osman.quizservice.service;

import com.osman.quizservice.feign.QuizInterface;
import com.osman.quizservice.model.QuestionWrapper;
import com.osman.quizservice.model.Quiz;
import com.osman.quizservice.model.Response;
import com.osman.quizservice.repo.QuizRepo;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
    public class QuizService {

    private final QuizRepo quizRepo;

    private final QuizInterface quizInterface;

    public QuizService(QuizRepo quizRepo,QuizInterface quizInterface) {
        this.quizRepo = quizRepo;
        this.quizInterface = quizInterface;
    }

    @Autowired
    CircuitBreakerRegistry registry;

    public ResponseEntity<String> createQuiz(String category, String title, int numOfQuestions) {

        List<Integer> questionIds = quizInterface.getQuestionsForQuiz(category,numOfQuestions).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);
        quizRepo.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuestionsForQuiz(int id) {

        Quiz quiz=quizRepo.findById(id).get();

        List<QuestionWrapper> questionForUsers=quizInterface.getQuestionsFromId(quiz.getQuestionIds()).getBody();

        return new ResponseEntity<>(questionForUsers, HttpStatus.OK);

    }

        @CircuitBreaker(name = "scoreCB", fallbackMethod = "getScoreFallback")
        public ResponseEntity<Integer> calculateResult(List<Response> responses) {

            System.out.println(registry.circuitBreaker("scoreCB").getState().name());
            Integer right=quizInterface.getScore(responses).getBody();

            return new ResponseEntity<>(right, HttpStatus.OK);
        }

        public ResponseEntity<Integer> getScoreFallback(List<Response> responses, Throwable ex){
            System.out.println(registry.circuitBreaker("scoreCB").getState().name());
            return new ResponseEntity<>(-2,HttpStatus.SERVICE_UNAVAILABLE);
        }
}
