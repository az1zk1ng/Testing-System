package com.company.testingsystem.service;

import com.company.testingsystem.db.Database;
import com.company.testingsystem.dto.QuestionDTO;
import com.company.testingsystem.dto.Response;
import com.company.testingsystem.files.WorkWithFiles;
import com.company.testingsystem.models.Question;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QuestionService {
    public Response add(QuestionDTO questionDTO) {

        if (questionDTO.subject() == null || questionDTO.subject().isBlank()) return new Response("Subject can't be empty",false);
        if (questionDTO.description() == null || questionDTO.description().isBlank()) return new Response("description can't be empty",false);
        if (questionDTO.wa1() == null || questionDTO.wa1().isBlank()) return new Response("Wrong Answer can't be empty",false);
        if (questionDTO.wa2() == null || questionDTO.wa2().isBlank()) return new Response("Wrong Answer can't be empty",false);
        if (questionDTO.ca() == null || questionDTO.ca().isBlank()) return new Response("Correct Answer can't be empty",false);


        Question question = new Question(UUID.randomUUID(), questionDTO.description(),
                new HashSet<>(Set.of(questionDTO.wa1(), questionDTO.wa2(), questionDTO.ca())),
                questionDTO.ca());

        Database.questionMap.get(questionDTO.subject()).add(question);
        WorkWithFiles.writeToQuestion(questionDTO.subject(), question);
        return new Response("Question added", true);
    }
}
