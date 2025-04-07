package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.Horse;
import com.example.horse_racing.Models.Race;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddBetFormServlet", urlPatterns = {"/add-bet-form"})
public class AddBetFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        List<Race> races = new ArrayList<>();
        List<Horse> horses = new ArrayList<>();

        String raceQuery = "SELECT id, start_time, end_time, location, create_at FROM races";
        String horseQuery = "SELECT id, name, age, breed, rider, create_at FROM horses";

        try (Connection conn = DBUtil.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(raceQuery);
                 ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    Race race = new Race(
                            rs.getInt("id"),
                            rs.getTimestamp("start_time"),
                            rs.getTimestamp("end_time"),
                            rs.getString("location"),
                            rs.getTimestamp("create_at")
                    );
                    races.add(race);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(horseQuery);
                 ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    Horse horse = new Horse(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("breed"),
                            rs.getString("rider"),
                            rs.getTimestamp("create_at")
                    );
                    horses.add(horse);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
            return;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("races", races);
        result.put("horses", horses);
        String json = new Gson().toJson(result);
        response.getWriter().write(json);
    }
}
