package ru.levelp.at.hw6.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public static void createTestChallenges() {
        List<Integer> ids = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT id FROM users ORDER BY id")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        }
        try (PreparedStatement statement
                 = connection.prepareStatement("INSERT INTO challenges VALUES (?, ?, ?, ?, ?)")) {
            for (int i = 2; i < ids.size(); i++) {
                statement.setString(1, i % 2 == 0 ? "white" : "black");
                statement.setDate(2, new Date(Instant.now().toEpochMilli()));
                statement.setString(3, UUID.randomUUID().toString());
                statement.setInt(4, ids.get(i));
                statement.setInt(5, ids.get(0));
                statement.execute();
                TimeUnit.MILLISECONDS.sleep(5);
            }
        }

    }

}
