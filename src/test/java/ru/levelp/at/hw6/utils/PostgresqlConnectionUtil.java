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
    public static void clearChallenges() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM challenges");
        }
    }

    @SneakyThrows
    public static void createTestChallenges() {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM users ORDER BY id")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO challenges (id, target_color, timestamp, uuid, from_id, to_id) VALUES (?, ?, ?, ?, ?, ?)"
        )) {
            for (int i = 2; i < ids.size(); i++) {
                int id;
                try (PreparedStatement idStatement
                         = connection.prepareStatement("SELECT nextval('hibernate_sequence') as num")) {
                    ResultSet resultSet = idStatement.executeQuery();
                    resultSet.next();
                    id = resultSet.getInt("num");
                }
                statement.setInt(1, id);
                statement.setString(2, i % 2 == 0 ? "white" : "black");
                statement.setTimestamp(3,
                    new Timestamp(Instant.now().atZone(ZoneId.of("GMT")).toInstant().toEpochMilli())
                );
                statement.setObject(4, UUID.randomUUID());
                statement.setInt(5, ids.get(i));
                statement.setInt(6, ids.get(0));
                statement.executeUpdate();
                TimeUnit.MILLISECONDS.sleep(5);
            }
        }

    }

}
