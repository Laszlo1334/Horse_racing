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

@WebServlet(name = "ListOfBetsServlet", urlPatterns = "/list-bets")
public class ListOfBetsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        List<BetDTO> bets = new ArrayList<>();
        String query = "SELECT id, race_id, horses_id, multyplier, bet_type, create_at FROM bets";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                BetDTO bet = new BetDTO();
                bet.setId(rs.getInt("id"));
                bet.setRaceId(rs.getInt("race_id"));
                bet.setHorsesId(rs.getInt("horses_id"));
                bet.setMultiplier(rs.getInt("multyplier"));
                bet.setBetType(rs.getString("bet_type"));
                bet.setCreateAt(rs.getTimestamp("create_at").toString());
                bets.add(bet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Не вдалося отримати список ставок.\"}");
            return;
        }

        String json = new Gson().toJson(bets);
        response.getWriter().write(json);
    }

    private static class BetDTO {
        private int id;
        private int raceId;
        private int horsesId;
        private int multyplier;
        private String betType;
        private String createAt;

        // Геттери і сеттери
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getRaceId() { return raceId; }
        public void setRaceId(int raceId) { this.raceId = raceId; }
        public int getHorsesId() { return horsesId; }
        public void setHorsesId(int horsesId) { this.horsesId = horsesId; }
        public int getMultiplier() { return multyplier; }
        public void setMultiplier(int multiplier) { this.multyplier = multiplier; }
        public String getBetType() { return betType; }
        public void setBetType(String betType) { this.betType = betType; }
        public String getCreateAt() { return createAt; }
        public void setCreateAt(String createAt) { this.createAt = createAt; }
    }
}