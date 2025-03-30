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

@WebServlet(name = "AddBetServlet", urlPatterns = "/add-bet")
public class AddBetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
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

        int raceId = json.get("raceId").getAsInt();
        int horsesId = json.get("horsesId").getAsInt();
        int multyplier = json.get("multyplier").getAsInt();
        String betType = json.get("betType").getAsString();

        try (Connection conn = DBUtil.getConnection()){
            String insertBetQuery = "INSERT INTO bets (race_id, horses_id, multyplier, bet_type, create_at) " +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            int betId;
            try (PreparedStatement stmt = conn.prepareStatement(insertBetQuery, Statement.RETURN_GENERATED_KEYS)){
                stmt.setInt(1, raceId);
                stmt.setInt(2, horsesId);
                stmt.setInt(3, multyplier);
                stmt.setString(4, betType);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()){
                    if (rs.next()) {
                        betId = rs.getInt(1);
                    } else {
                        throw new Exception("Не вдалося отримати ID ставки.");
                    }
                }
            }
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("betId", betId);
            response.getWriter().write(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Не вдалося додати ставку: " + e.getMessage() + "\"}");
        }
    }
}