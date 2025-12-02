package com.osman.questionservice.controller;

import com.osman.questionservice.model.Question;
import com.osman.questionservice.model.QuestionWrapper;
import com.osman.questionservice.model.Response;
import com.osman.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    private final Environment environment;

    @Autowired
    public QuestionController(QuestionService questionService, Environment environment) {
        this.questionService = questionService;
        this.environment = environment;
    }

    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return new ResponseEntity<>(questionService.getAllQuestions(), HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return new ResponseEntity<>(questionService.getQuestionsByCategory(category), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question) {
        return new ResponseEntity<>(questionService.addQuestion(question), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable int id) {
        return new ResponseEntity<>(questionService.deleteQuestion(id), HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable int id, @RequestBody Question updatedQuestion) {
        String result = questionService.updateQuestion(id, updatedQuestion);
        if (result.equals("Question updated successfully.")) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String category, @RequestParam int numOfQuestions){
        return new ResponseEntity<>(questionService.getQuestionForService(category, numOfQuestions), HttpStatus.OK);
    }

    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        System.out.println(environment.getProperty("local.server.port"));
        return new ResponseEntity<>(questionService.getQuestionsFromId(questionIds), HttpStatus.OK);
    }

    @PostMapping("/getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        return new ResponseEntity<>(questionService.getScore(responses), HttpStatus.OK);
    }

    @GetMapping("/sorted")
    public ResponseEntity<List<Question>> getSortedQuestions(@RequestParam String sortBy) {
        List<Question> sortedQuestions = questionService.getSortedQuestions(sortBy);
        return new ResponseEntity<>(sortedQuestions, HttpStatus.OK);
    }

    @GetMapping("/paged")
    public ResponseEntity<List<Question>> getPagedQuestions(@RequestParam int pageNo, @RequestParam int pageSize) {
        List<Question> pagedQuestions = questionService.getPagedQuestions(pageNo, pageSize);
        return new ResponseEntity<>(pagedQuestions, HttpStatus.OK);
    }


}
