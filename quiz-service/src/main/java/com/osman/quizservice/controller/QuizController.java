package com.osman.quizservice.controller;

import com.osman.quizservice.model.QuestionWrapper;
import com.osman.quizservice.model.Response;
import com.osman.quizservice.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam String title, @RequestParam int numOfQuestions){
        return new ResponseEntity<>(quizService.createQuiz(category,title,numOfQuestions), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsForQuiz(@PathVariable int id){
        return new ResponseEntity<>(quizService.getQuestionsForQuiz(id), HttpStatus.OK);
    }

    @PostMapping("/submit")
    public ResponseEntity<Integer> submitQuiz(@RequestBody List<Response> responses){
        return new ResponseEntity<>(quizService.calculateResult(responses), HttpStatus.OK);
    }



}
