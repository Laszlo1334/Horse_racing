package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.EditBetRequest;
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

@WebServlet(name = "EditBetServlet", urlPatterns = {"/edit-bet"})
public class EditBetServlet extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }

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
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        EditBetRequest editRequest = new Gson().fromJson(sb.toString(), EditBetRequest.class);

        if (editRequest == null || editRequest.getPlacedBetId() <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid request data\"}");
            return;
        }

        if (editRequest.getNewState() == null ||
                (!editRequest.getNewState().equals("WIN") && !editRequest.getNewState().equals("LOSE"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid bet state. Allowed values are WIN or LOSE.\"}");
            return;
        }

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String updatePlacedBets = "UPDATE placed_bets SET state = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updatePlacedBets)) {
                ps.setString(1, editRequest.getNewState());
                ps.setInt(2, editRequest.getPlacedBetId());
                int updatedRows = ps.executeUpdate();

                if(updatedRows == 0) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\": \"Bet not found.\"}");
                    return;
                }
            }

            response.getWriter().write("{\"message\": \"Bet updated successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (Exception e) { /* Ігнорувати */ }
            }
        }
    }
}
