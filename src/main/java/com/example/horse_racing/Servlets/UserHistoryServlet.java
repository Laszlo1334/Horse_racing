package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.UserHistory;
import com.example.horse_racing.Utils.DBUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserHistoryServlet", urlPatterns = "/user-history")
public class UserHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Email parameter is required\"}");
            return;
        }

        List<UserHistory> historyList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();

            int userId = -1;
            String userQuery = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement userStmt = conn.prepareStatement(userQuery)) {
                userStmt.setString(1, email);
                try (ResultSet userRs = userStmt.executeQuery()) {
                    if (userRs.next()) {
                        userId = userRs.getInt("id");
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\": \"User not found\"}");
                        return;
                    }
                }
            }

            String query = "SELECT pb.id AS placed_bet_id, pb.user_id, b.id AS bet_id, r.id AS race_id, " +
                    "r.start_time, r.end_time, r.location, " +
                    "h.id AS horse_id, h.name AS horse_name, h.age, h.breed, h.rider, " +
                    "b.multyplier, b.bet_type, pb.sum, pb.state, pb.create_at " +
                    "FROM placed_bets pb " +
                    "JOIN bets b ON pb.bet_id = b.id " +
                    "JOIN races r ON b.race_id = r.id " +
                    "JOIN horses h ON b.horses_id = h.id " +
                    "WHERE pb.user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        UserHistory history = new UserHistory(
                                rs.getInt("placed_bet_id"),
                                rs.getInt("user_id"),
                                rs.getInt("bet_id"),
                                rs.getInt("race_id"),
                                rs.getTimestamp("start_time"),
                                rs.getTimestamp("end_time"),
                                rs.getString("location"),
                                rs.getInt("horse_id"),
                                rs.getString("horse_name"),
                                rs.getInt("age"),
                                rs.getString("breed"),
                                rs.getString("rider"),
                                rs.getBigDecimal("multyplier"),
                                rs.getString("bet_type"),
                                rs.getBigDecimal("sum"),
                                rs.getString("state"),
                                rs.getTimestamp("create_at")
                        );
                        historyList.add(history);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            return;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception ex) { }
        }

        String json = new Gson().toJson(historyList);
        response.getWriter().write(json);
    }
}
