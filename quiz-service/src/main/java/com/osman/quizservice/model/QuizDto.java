package com.osman.quizservice.model;

import lombok.Data;

@Data
public class QuizDto {

    private String category;
    private String title;
    private int numOfQuestions;
}
