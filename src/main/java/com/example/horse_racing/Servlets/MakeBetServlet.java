package com.example.horse_racing.Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.example.horse_racing.Utils.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()){
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);

        int betId = json.get("betId").getAsInt();
        int userId = json.get("userId").getAsInt();
        String state = json.get("state").getAsString();
        int sum = json.get("sum").getAsInt();

        try (Connection conn = DBUtil.getConnection()){
            String insertPlaceBetQuery = "INSERT INTO place_bets (bet_id, user_id, state, sum, create_at) " +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            int placedBetId;
            try (PreparedStatement stmt = conn.prepareStatement(insertPlaceBetQuery, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, betId);
                stmt.setInt(2, userId);
                stmt.setString(3, state);
                stmt.setInt(4, sum);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        placedBetId = rs.getInt(1);
                    } else {
                        throw new Exception("Не вдалося отримати ID зробленої ставки.");
                    }
                }
            }
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("placedBetId", placedBetId);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Не вдалося зробити ставку: " + e.getMessage() + "\"}");
        }
    }
}