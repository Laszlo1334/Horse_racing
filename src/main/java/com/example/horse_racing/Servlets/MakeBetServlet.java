package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.MakeBetRequest;
import com.example.horse_racing.Utils.DBUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

@WebServlet(name = "MakeBetServlet", urlPatterns = "/make-bet")
public class MakeBetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        try (BufferedReader reader = request.getReader()) {
            Gson gson = new Gson();
            MakeBetRequest betRequest = gson.fromJson(reader, MakeBetRequest.class);

            int betId = betRequest.getBetId();
            String email = betRequest.getEmail();
            BigDecimal betSum = betRequest.getSum();

            try (Connection conn = DBUtil.getConnection()) {
                conn.setAutoCommit(false);

                String userQuery = "SELECT id, wallet FROM users WHERE email = ?";
                int userId;
                BigDecimal currentWallet;
                try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                    userStmt.setString(1, email);
                    try (ResultSet userRs = userStmt.executeQuery()) {
                        if (userRs.next()) {
                            userId = userRs.getInt("id");
                            currentWallet = userRs.getBigDecimal("wallet");
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"error\": \"User not found\"}");
                            return;
                        }
                    }
                }

                if (currentWallet.compareTo(betSum) < 0) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Insufficient funds\"}");
                    return;
                }

                String betQuery = "SELECT * FROM bets WHERE id = ?";
                try (PreparedStatement betStmt = conn.prepareStatement(betQuery)) {
                    betStmt.setInt(1, betId);
                    try (ResultSet betRs = betStmt.executeQuery()) {
                        if (!betRs.next()) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"error\": \"Bet not found\"}");
                            return;
                        }
                    }
                }

                String insertQuery = "INSERT INTO placed_bets (bet_id, user_id, state, sum, create_at) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, betId);
                    insertStmt.setInt(2, userId);
                    insertStmt.setString(3, "ACTIVE");
                    insertStmt.setBigDecimal(4, betSum);
                    insertStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                    int affectedRows = insertStmt.executeUpdate();
                    if (affectedRows <= 0) {
                        conn.rollback();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("{\"error\": \"Failed to place bet\"}");
                        return;
                    }
                }

                String updateQuery = "UPDATE users SET wallet = wallet - ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setBigDecimal(1, betSum);
                    updateStmt.setInt(2, userId);
                    int updatedRows = updateStmt.executeUpdate();
                    if (updatedRows <= 0) {
                        conn.rollback();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        response.getWriter().write("{\"error\": \"Failed to update wallet\"}");
                        return;
                    }
                }

                conn.commit();

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"Bet placed successfully\"}");
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
