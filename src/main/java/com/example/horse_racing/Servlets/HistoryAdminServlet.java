package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.HistoryAdmin;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HistoryAdminServlet", urlPatterns = "/admin-history")
public class HistoryAdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        List<HistoryAdmin> historyList = new ArrayList<>();

        String query = "SELECT pb.id AS placed_bet_id, b.id AS bet_id, pb.user_id, pb.sum, pb.state, b.multyplier, pb.create_at " +
                "FROM placed_bets pb " +
                "JOIN bets b ON pb.bet_id = b.id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HistoryAdmin history = new HistoryAdmin(
                        rs.getInt("placed_bet_id"),
                        rs.getInt("bet_id"),
                        rs.getInt("user_id"),
                        rs.getBigDecimal("sum"),
                        rs.getString("state"),
                        rs.getBigDecimal("multyplier"),
                        rs.getTimestamp("create_at")
                );
                historyList.add(history);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            return;
        }

        String json = new Gson().toJson(historyList);
        response.getWriter().write(json);
    }
}
