package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;

public class OracleDatabaseTest {

    @Test
    public void writeAndReadUser() throws SQLException {

    	String url = requiredEnv("DB_URL");
        String username = requiredEnv("DB_USER");
        String password = requiredEnv("DB_PASSWORD");

        String userId = UUID.randomUUID().toString();
        String expectedName = "Test User";
        String expectedEmail = "test.user@example.com";

        try (Connection connection = DriverManager.getConnection(
                url, username, password)) {

            // Control the transaction ourselves
            connection.setAutoCommit(false);

            try {
                // WRITE: Insert a test record
                String insertSql = """
                        INSERT INTO automation_test_users
                            (user_id, user_name, email)
                        VALUES (?, ?, ?)
                        """;

                try (PreparedStatement insert =
                             connection.prepareStatement(insertSql)) {

                    insert.setString(1, userId);
                    insert.setString(2, expectedName);
                    insert.setString(3, expectedEmail);

                    int insertedRows = insert.executeUpdate();

                    Assert.assertEquals(
                            insertedRows, 1,
                            "Exactly one row should be inserted"
                    );

                    System.out.println("WRITE: User inserted");
                }

                // READ: Fetch the record using its unique ID
                String selectSql = """
                        SELECT user_id, user_name, email
                        FROM automation_test_users
                        WHERE user_id = ?
                        """;

                try (PreparedStatement select =
                             connection.prepareStatement(selectSql)) {

                    select.setString(1, userId);

                    try (ResultSet result = select.executeQuery()) {

                        Assert.assertTrue(
                                result.next(),
                                "Inserted user should exist"
                        );

                        Assert.assertEquals(
                                result.getString("user_id"),
                                userId
                        );

                        Assert.assertEquals(
                                result.getString("user_name"),
                                expectedName
                        );

                        Assert.assertEquals(
                                result.getString("email"),
                                expectedEmail
                        );

                        Assert.assertFalse(
                                result.next(),
                                "Only one user should match"
                        );

                        System.out.println(
                                "READ: User values verified"
                        );
                    }
                }

            } finally {
                // Remove the test insert, even if an assertion fails
                connection.rollback();
                System.out.println(
                        "CLEANUP: Transaction rolled back"
                );
            }
        }
    }

    private static String requiredEnv(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing environment variable: " + name
            );
        }

        return value;
    }
}