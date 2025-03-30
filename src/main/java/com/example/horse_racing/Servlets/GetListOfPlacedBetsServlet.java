package com.example.horse_racing.Servlets;

import com.google.gson.Gson;
import com.example.horse_racing.Utils.DBUtil;
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

@WebServlet(name = "GetListOfPlacedBetsServlet", urlPatterns = "/list-placed-bets")
public class GetListOfPlacedBetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String userIdParam = request.getParameter("userId");
        List<PlacedBetDTO> placedBets = new ArrayList<>();
        String query;
        boolean filterByUser = false;
        if (userIdParam != null && !userIdParam.isEmpty()) {
            filterByUser = true;
            query = "SELECT id, bet_id, user_id, state, sum, create_at FROM place_bets WHERE user_id = ?";
        } else {
            query = "SELECT id, bet_id, user_id, state, sum, create_at FROM place_bets";
        }
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            if (filterByUser) {
                int userId = Integer.parseInt(userIdParam);
                stmt.setInt(1, userId);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PlacedBetDTO pb = new PlacedBetDTO();
                    pb.setId(rs.getInt("id"));
                    pb.setBetId(rs.getInt("bet_id"));
                    pb.setUserId(rs.getInt("user_id"));
                    pb.setState(rs.getString("state"));
                    pb.setSum(rs.getInt("sum"));
                    pb.setCreateAt(rs.getTimestamp("create_at").toString());
                    placedBets.add(pb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Не вдалося отримати список зроблених ставок.\"}");
            return;
        }
        String json = new Gson().toJson(placedBets);
        response.getWriter().write(json);
    }

    private static class PlacedBetDTO {
        private int id;
        private int betId;
        private int userId;
        private String state;
        private int sum;
        private String createAt;

        // Геттери і сеттери
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getBetId() { return betId; }
        public void setBetId(int betId) { this.betId = betId; }
        public int getUserId() { return userId; }
        public void setUserId(int userId) { this.userId = userId; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public int getSum() { return sum; }
        public void setSum(int sum) { this.sum = sum; }
        public String getCreateAt() { return createAt; }
        public void setCreateAt(String createAt) { this.createAt = createAt; }
    }
}