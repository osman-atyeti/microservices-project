package com.osman.quizservice.service;

import com.netflix.discovery.converters.Auto;
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

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    public QuizService(QuizRepo quizRepo, QuizInterface quizInterface, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.quizRepo = quizRepo;
        this.quizInterface = quizInterface;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public String createQuiz(String category, String title, int numOfQuestions) {

        List<Integer> questionIds = quizInterface.getQuestionsForQuiz(category,numOfQuestions).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questionIds);
        quizRepo.save(quiz);

        return "Success";
    }


    public List<QuestionWrapper> getQuestionsForQuiz(int id) {

        Quiz quiz=quizRepo.findById(id).get();

        List<QuestionWrapper> questionForUsers=quizInterface.getQuestionsFromId(quiz.getQuestionIds()).getBody();

        return questionForUsers;

    }

        @CircuitBreaker(name = "scoreCB", fallbackMethod = "getScoreFallback")
        public Integer calculateResult(List<Response> responses) {

            System.out.println(circuitBreakerRegistry.circuitBreaker("scoreCB").getState().name());
            Integer right=quizInterface.getScore(responses).getBody();

            return right;
        }

        public ResponseEntity<Integer> getScoreFallback(List<Response> responses, Throwable ex){
            System.out.println(circuitBreakerRegistry.circuitBreaker("scoreCB").getState().name());
            return new ResponseEntity<>(-2,HttpStatus.SERVICE_UNAVAILABLE);
        }
}
