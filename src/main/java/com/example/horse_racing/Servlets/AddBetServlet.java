package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.AddBetRequest;
import com.example.horse_racing.Utils.DBUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet(name = "AddBetServlet", urlPatterns = "/add-bet")
public class AddBetServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        AddBetRequest addBetRequest = new Gson().fromJson(sb.toString(), AddBetRequest.class);
        if (addBetRequest == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid request data\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String insertSQL = "INSERT INTO bets (race_id, horses_id, multyplier, bet_type, create_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, addBetRequest.getRaceId());
                ps.setInt(2, addBetRequest.getHorseId());
                ps.setBigDecimal(3, addBetRequest.getMultiplier());
                ps.setString(4, addBetRequest.getBetType());
                ps.executeUpdate();
            }
            response.getWriter().write("{\"message\":\"Bet added successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        } finally {
            if(conn != null) {
                try { conn.close(); } catch(Exception ex) { }
            }
        }
    }
}
