package com.company.testingsystem;

import com.company.testingsystem.db.Database;
import com.company.testingsystem.dto.QuestionDTO;
import com.company.testingsystem.dto.Response;
import com.company.testingsystem.enums.Role;
import com.company.testingsystem.models.Question;
import com.company.testingsystem.models.User;
import com.company.testingsystem.service.QuestionService;
import com.company.testingsystem.service.SubjectService;
import com.company.testingsystem.service.UserService;
import com.company.testingsystem.utils.ScannerUtil;

import java.util.*;

public class Application {
    static {
        Database.loadData();
    }
    public static void main(String[] args) {
        while (true){
            System.out.print("""
                    1) Login
                    2) Registration
                    0) Exit
                    :?
                    """);
            String t = ScannerUtil.SCANNER_STR.nextLine();

            if(t.equals("0")) break;

            switch (t){
                case "1" -> login();
                case "2" -> registration();
            }
        }
    }

    private static void registration() {
        // todo
        System.out.println("Enter FullName :");
        String fullName = ScannerUtil.SCANNER_STR.nextLine().trim();
        System.out.println("Enter Username :");
        String userName = ScannerUtil.SCANNER_STR.nextLine().trim();
        System.out.println("Enter Password :");
        String passwd = ScannerUtil.SCANNER_STR.nextLine();
        System.out.println("Confirm Password :");
        String confirmPasswd = ScannerUtil.SCANNER_STR.nextLine().trim();

        System.out.println(UserService.regCheckParams(fullName, userName, passwd, confirmPasswd));

    }

    private static void login() {
        System.out.println("username: ");
        String username = ScannerUtil.SCANNER_STR.nextLine();
        System.out.println("password: ");
        String password = ScannerUtil.SCANNER_STR.nextLine();

        UserService userService = new UserService();
        User sessionUser = userService.login(username,password);

        if (sessionUser == null) {
            System.out.println("user not found");
        }else {
            cabinet(sessionUser);
        }
    }

    private static void cabinet(User sessionUser) {
        System.out.println("Welcome " + sessionUser.getFullName());

        if (sessionUser.getRole().equals(Role.TEACHER)){
            teacherPage(sessionUser);
        }else {
            studentPage(sessionUser);
        }
    }

    private static void studentPage(User sessionUser) {
        // todo
    }

    private static void teacherPage(User sessionUser) {
        while (true) {
            System.out.println("""
                    1) Add subject
                    2) Show subject list
                    3) Add question
                    4) Show question list
                    5) Show user list
                    6) Change password
                    0) Log out
                    :?             
                      """);
            String option = ScannerUtil.SCANNER_STR.nextLine();

            if (option.equals("0")) break;

            switch (option){
                case "1" -> addSubject();
                case "2" -> showSubjectList();
                case "3" -> addQuestion();
                case "4" -> showQuestionList();
                case "5" -> showUserList(new ArrayList<User>(Database.solveMap.keySet()));
                case "6" -> changePassword(sessionUser);
            }
        }
    }

    private static void changePassword(User sessionUser) {

        String cpaas = ScannerUtil.SCANNER_STR.nextLine();
        if (!cpaas.equals(sessionUser.getPassword())) return;
        System.out.println(UserService.changePasswd(sessionUser));
    }

    private static void showUserList(ArrayList<User> users) {
        users.forEach(System.out::println);
    }

    private static void showQuestionList() {
        Set<String> subjects = Database.questionMap.keySet();
        if (subjects.isEmpty()) {
            System.out.println("No questions");
        }else {
            subjects.forEach(System.out::println);
            System.out.print("Enter subject name: ");
            String subject = ScannerUtil.SCANNER_STR.nextLine();
            subject = subject.trim().toUpperCase();

            if (subjects.contains(subject)) {
                List<Question> questions = Database.questionMap.get(subject);
                if (questions.isEmpty()) {
                    System.out.println("No questions by "+subject);
                }
                questions.forEach(System.out::println);
            }else{
                System.out.println("No that subject");
            }
        }
    }

    private static void addQuestion() {
        Set<String> subjects = Database.questionMap.keySet();
        if (subjects.isEmpty()) {
            System.out.println("No subjects");
            return;
        }

        subjects.forEach(System.out::println);
        System.out.print("Enter subject name: ");
        String subject = ScannerUtil.SCANNER_STR.nextLine();
        subject = subject.trim().toUpperCase();

        if (!subjects.contains(subject)) {
            System.out.println("No that subject");
            return;
        }

        System.out.println("Enter description: ");
        String description = ScannerUtil.SCANNER_STR.nextLine();
        System.out.println("Enter correct answer: ");
        String correctAnswer = ScannerUtil.SCANNER_STR.nextLine();
        System.out.println("Enter 1-wrong answer: ");
        String wa1 = ScannerUtil.SCANNER_STR.nextLine();
        System.out.println("Enter 2-wrong answer: ");
        String wa2 = ScannerUtil.SCANNER_STR.nextLine();

        QuestionService questionService = new QuestionService();
        QuestionDTO questionDTO = new QuestionDTO(subject,
                description, correctAnswer, wa1, wa2);
        Response response = questionService.add(questionDTO);
        System.out.println(response);
    }

    private static void showSubjectList() {
        Set<String> subjects = Database.questionMap.keySet();
        if (subjects.isEmpty()) {
            System.out.println("No subjects");
        }else {
            subjects.forEach(System.out::println);
        }
    }

    private static void addSubject() {
        System.out.print("Enter subject name: ");
        String subject = ScannerUtil.SCANNER_STR.nextLine();

        SubjectService subjectService = new SubjectService();
        Response response = subjectService.add(subject);

        System.out.println(response);

    }


}
