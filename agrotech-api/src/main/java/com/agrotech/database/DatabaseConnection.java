
package com.agrotech.database;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:h2:file:./data/agrotech";

    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {

        try {
            // Cria o diretório onde o banco será armazenado
            Files.createDirectories(Path.of("./data"));

            // Lê o arquivo SQL
            InputStream input = DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("schema.sql");

            if (input == null) {
                throw new IllegalStateException(
                        "Arquivo schema.sql não encontrado!"
                );
            }

            String sql;

            try (input) {
                sql = new String(
                        input.readAllBytes(),
                        StandardCharsets.UTF_8
                );
            }

            // Executa os comandos SQL
            try (Connection connection = getConnection();
                 var statement = connection.createStatement()) {

                for (String command : sql.split(";")) {
                    if (!command.isBlank()) {
                        statement.execute(command);
                    }
                }
            }

            System.out.println("Banco de dados inicializado!");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao inicializar o banco de dados", e
            );
        }
    }
}
