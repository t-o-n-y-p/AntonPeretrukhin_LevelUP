package ru.levelp.at.hw6.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgresqlConnectionUtil {

    private PostgresqlConnectionUtil() {
    }
    
    private static final String startingPositionFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String startingPositionLegalMoves
        = "a2a3 b2b3 c2c3 d2d3 e2e3 f2f3 g2g3 h2h3 a2a4 b2b4 c2c4 d2d4 e2e4 f2f4 g2g4 h2h4 b1a3 b1c3 g1f3 g1h3";
    private static final String url = "jdbc:postgresql://localhost:5432/chess";
    private static final String username = "postgres";
    private static final String password = "root";

    private static Connection connection;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            log.error("Failed to find PostgreSQL driver", e);
        } catch (SQLException e) {
            log.error("Failed to connect to PostgreSQL database", e);
        }
    }

    @SneakyThrows
    public static void clearData() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM moves");
            statement.execute("DELETE FROM games");
            statement.execute("DELETE FROM challenges");
            statement.execute("DELETE FROM users");
        }
    }

    @SneakyThrows
    public static void clearUserActivity() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM moves");
            statement.execute("DELETE FROM games");
            statement.execute("DELETE FROM challenges");
        }
    }

    @SneakyThrows
    public static void createTestChallenges() {
        List<Integer> ids = getUserIds(12);
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO challenges (id, target_color, timestamp, uuid, from_id, to_id) VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            for (int i = 2; i < ids.size(); i++) {
                int id = getId();
                statement.setInt(1, id);
                statement.setString(2, i % 2 == 0 ? "white" : "black");
                statement.setTimestamp(3,
                    new Timestamp(Instant.now().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
                );
                statement.setObject(4, UUID.randomUUID());
                statement.setInt(5, ids.get(i));
                statement.setInt(6, ids.get(0));
                statement.addBatch();
                TimeUnit.MILLISECONDS.sleep(5);
            }
            statement.executeBatch();
        }
    }

    @SneakyThrows
    public static void createTestGame() {
        List<Integer> ids = getUserIds(2);
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO games ("
                    + "id, description, fen, is_completed, last_modified_timestamp, legal_moves, uuid, "
                    + "black_id, player_to_move_id, white_id"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        )) {
            int id = getId();
            statement.setInt(1, id);
            statement.setString(2, null);
            statement.setString(3, startingPositionFen);
            statement.setBoolean(4, false);
            statement.setTimestamp(5,
                new Timestamp(Instant.now().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
            );
            statement.setString(6, startingPositionLegalMoves);
            statement.setObject(7, UUID.randomUUID());
            statement.setInt(8, ids.get(1));
            statement.setInt(9, ids.get(0));
            statement.setInt(10, ids.get(0));
            statement.execute();
            TimeUnit.MILLISECONDS.sleep(5);
        }
    }

    @SneakyThrows
    private static List<Integer> getUserIds(int limit) {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM users ORDER BY id LIMIT ?")) {
            statement.setInt(1, limit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        }
        return ids;
    }

    @SneakyThrows
    private static int getId() {
        try (PreparedStatement idStatement
                 = connection.prepareStatement("SELECT nextval('hibernate_sequence') as num")) {
            ResultSet resultSet = idStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("num");
        }
    }
}
