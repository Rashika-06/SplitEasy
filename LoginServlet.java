package com.spliteasy.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.spliteasy.model.User;
import com.spliteasy.db.DatabaseHelper;

@WebServlet("/split")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response type
        response.setContentType("text/plain;charset=UTF-8");
        
        // Get parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validation
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            response.getWriter().write("Please enter username and password!");
            return;
        }
        
        // Try to login
        User user = DatabaseHelper.loginUser(username.trim(), password);
        
        if (user != null) {
            // Create session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("userid", user.getId());
            
            response.getWriter().write("Login Success! Welcome " + user.getFullname());
        } else {
            response.getWriter().write("Invalid username or password!");
        }
    }
}
