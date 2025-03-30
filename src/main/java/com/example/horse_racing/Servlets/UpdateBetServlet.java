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

@WebServlet(name = "UpdateBetServlet", urlPatterns = "/update-bet")
public class UpdateBetServlet extends HttpServlet {
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

        int id = json.get("id").getAsInt();
        int raceId = json.get("raceId").getAsInt();
        int horsesId = json.get("horsesId").getAsInt();
        int multiplier = json.get("multiplier").getAsInt();
        String betType = json.get("betType").getAsString();

        try (Connection conn = DBUtil.getConnection()){
            String updateQuery = "UPDATE bets SET race_id = ?, horses_id = ?, multiplier = ?, bet_type = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)){
                stmt.setInt(1, raceId);
                stmt.setInt(2, horsesId);
                stmt.setInt(3, multiplier);
                stmt.setString(4, betType);
                stmt.setInt(5, id);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    response.getWriter().write("{\"success\": true}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"Ставку не знайдено або не вдалося оновити.\"}");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Не вдалося оновити ставку: " + e.getMessage() + "\"}");
        }
    }
}