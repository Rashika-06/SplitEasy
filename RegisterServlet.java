package com.spliteasy.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spliteasy.model.User;
import com.spliteasy.db.DatabaseHelper;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response type
        response.setContentType("text/plain;charset=UTF-8");
        
        // Get parameters
        String fullname = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validation
        if (fullname == null || fullname.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            response.getWriter().write("All fields are required!");
            return;
        }
        
        if (password.length() < 4) {
            response.getWriter().write("Password must be at least 4 characters!");
            return;
        }
        
        // Create user object and register
        User user = new User(fullname.trim(), username.trim(), password);
        String result = DatabaseHelper.registerUser(user);
        
        response.getWriter().write(result);
    }
}
