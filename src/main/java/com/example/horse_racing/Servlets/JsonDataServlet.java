package com.example.horse_racing.Servlets;

import com.example.horse_racing.Models.User;
import com.example.horse_racing.Models.Horse;
import com.example.horse_racing.Models.Bet;
import com.example.horse_racing.Models.PlacedBet;
import com.example.horse_racing.Models.Race;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "JsonDataServlet", urlPatterns = "/data")
public class JsonDataServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/Horse_racing";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "135VlAdIsLaV";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        // Явная загрузка драйвера (если требуется)
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Driver not found\"}");
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            List<User> users = getUsers(connection);
            List<Race> races = getRaces(connection);
            List<Horse> horses = getHorses(connection);
            List<Bet> bets = getBets(connection);
            List<PlacedBet> placedBets = getPlacedBets(connection);

            DataResponse dataResponse = new DataResponse(users, races, horses, bets, placedBets);
            out.print(objectMapper.writeValueAsString(dataResponse));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Database error\"}");
        }
        out.flush();
    }

    private List<User> getUsers(Connection connection) throws Exception {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Проверьте, что имена столбцов соответствуют вашим таблицам
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getBigDecimal("wallet"),
                        rs.getTimestamp("create_at").toLocalDateTime()  // Возможно, должно быть created_at
                ));
            }
        }
        return users;
    }

    private List<Race> getRaces(Connection connection) throws Exception {
        List<Race> races = new ArrayList<>();
        String query = "SELECT * FROM races";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                races.add(new Race(
                        rs.getInt("id"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("location"),
                        rs.getTimestamp("create_at").toLocalDateTime()  // Проверьте имя поля
                ));
            }
        }
        return races;
    }

    private List<Horse> getHorses(Connection connection) throws Exception {
        List<Horse> horses = new ArrayList<>();
        String query = "SELECT * FROM horses";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                horses.add(new Horse(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("breed"),
                        rs.getString("rider"),
                        rs.getTimestamp("create_at").toLocalDateTime()  // Проверьте имя поля
                ));
            }
        }
        return horses;
    }

    private List<Bet> getBets(Connection connection) throws Exception {
        List<Bet> bets = new ArrayList<>();
        String query = "SELECT * FROM bets";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bets.add(new Bet(
                        rs.getInt("id"),
                        rs.getInt("race_id"),
                        rs.getInt("horses_id"),
                        rs.getBigDecimal("multyplier"), // Если в таблице поле называется multiplier, исправьте здесь
                        rs.getString("bet_type"),
                        rs.getTimestamp("create_at").toLocalDateTime()  // Проверьте имя поля
                ));
            }
        }
        return bets;
    }

    private List<PlacedBet> getPlacedBets(Connection connection) throws Exception {
        List<PlacedBet> placedBets = new ArrayList<>();
        String query = "SELECT * FROM placed_bets";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                placedBets.add(new PlacedBet(
                        rs.getInt("id"),
                        rs.getInt("bet_id"),
                        rs.getInt("user_id"),
                        rs.getString("state"),
                        rs.getBigDecimal("sum"),
                        rs.getTimestamp("create_at").toLocalDateTime()  // Проверьте имя поля
                ));
            }
        }
        return placedBets;
    }

    // Вспомогательный класс для формирования ответа
    private static class DataResponse {
        public List<User> users;
        public List<Race> races;
        public List<Horse> horses;
        public List<Bet> bets;
        public List<PlacedBet> placedBets;

        public DataResponse(List<User> users, List<Race> races, List<Horse> horses, List<Bet> bets, List<PlacedBet> placedBets) {
            this.users = users;
            this.races = races;
            this.horses = horses;
            this.bets = bets;
            this.placedBets = placedBets;
        }
    }
}