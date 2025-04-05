package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.UserWallet;
import com.example.horse_racing.Utils.DBUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@WebServlet(name = "UserWalletServlet", urlPatterns = "/user-wallet")
public class UserWalletServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String email = request.getParameter("email");
        if(email == null || email.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Email is required\"}");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT id, email, wallet, create_at FROM users WHERE email = ?";
            try(PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setString(1, email);
                try(ResultSet rs = stmt.executeQuery()){
                    if(rs.next()){
                        int id = rs.getInt("id");
                        String emailFromDB = rs.getString("email");
                        // Отримуємо значення балансу
                        BigDecimal wallet = rs.getBigDecimal("wallet");
                        Timestamp createAt = rs.getTimestamp("create_at");
                        UserWallet userWallet = new UserWallet(id, emailFromDB, wallet, createAt);
                        String json = new Gson().toJson(userWallet);
                        response.getWriter().write(json);
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\": \"User not found\"}");
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
