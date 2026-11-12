
package com.agrotech.server;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.agrotech.controller.EquipmentController;
import com.sun.net.httpserver.HttpServer;

public class ApiServer {

    public static void start() {

        try {

            HttpServer server = HttpServer.create(
                    new InetSocketAddress(8080),
                    0
            );

            server.createContext("/api/health", exchange -> {

                String response = """
                        {
                            "status": "UP",
                            "application": "AgroTech API"
                        }
                        """;

                exchange.getResponseHeaders().set(
                        "Content-Type",
                        "application/json; charset=UTF-8"
                );

                byte[] responseBytes =
                        response.getBytes(StandardCharsets.UTF_8);

                exchange.sendResponseHeaders(
                        200,
                        responseBytes.length
                );

                try (OutputStream output =
                             exchange.getResponseBody()) {

                    output.write(responseBytes);
                }
            });

server.createContext(
        "/api/equipments",
        new EquipmentController()
);

            server.start();

            System.out.println(
                    "Servidor iniciado em http://localhost:8080"
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao iniciar servidor HTTP", e
            );
        }
    }
}
