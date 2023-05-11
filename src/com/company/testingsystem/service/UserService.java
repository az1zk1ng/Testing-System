package com.company.testingsystem.service;

import com.company.testingsystem.db.Database;
import com.company.testingsystem.dto.Response;
import com.company.testingsystem.models.User;
import com.company.testingsystem.utils.ScannerUtil;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class UserService {

    public static Response changePasswd(User sessionUser) {

        System.out.println("Enter new Password");
        String pass1 = ScannerUtil.SCANNER_STR.nextLine();
        if (pass1.equals(sessionUser.getPassword())) return new Response("You cannot enter your old password when changing your password",false);
        System.out.println("Enter new Password Again");
        String pass2 = ScannerUtil.SCANNER_STR.nextLine();
        if (!pass2.equals(pass1)) return new Response("password is not equal",false);
        sessionUser.setPassword(pass2);
        return new Response("Password successfully changed",true);
    }

    public static Response regCheckParams(String fullName, String userName, String passwd, String confirmPasswd) {

        if (fullName == null || fullName.isBlank()) return new Response("Full name can't be empty !",false);
        if (userName == null || userName.isBlank()) return new Response("Username can't be empty !",false);
        if (passwd == null || passwd.isBlank() || passwd.length() <= 4) return new Response("Password can't be empty and lower 4 !",false);
        if (confirmPasswd == null || confirmPasswd.isBlank() || !confirmPasswd.equals(passwd)) return new Response("Confirm password be equals Password and can't be empty",false);
        Database.solveMap.put(new User(UUID.randomUUID(),fullName,userName,passwd),new ArrayList<>());
        return new Response("Success",true);
    }

    public User login(String username, String password) {
        if ((username == null && username.isBlank()) || (password == null && password.isBlank())) {
            Throwable NullPointerException = new Throwable();
            RuntimeException runtimeException = new RuntimeException("username or password empty",NullPointerException);
            throw runtimeException;
        } else {
            Set<User> users = Database.solveMap.keySet();

            for (User user : users) {
                if (user.getUsername().equals(username) &&
                        user.getPassword().equals(password)) {
                    return user;
                }
            }
            return null;
        }
    }
}
