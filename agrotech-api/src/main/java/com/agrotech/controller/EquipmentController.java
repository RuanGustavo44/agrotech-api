
package com.agrotech.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.agrotech.model.Equipment;
import com.agrotech.service.EquipmentService;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class EquipmentController implements HttpHandler {

    private final EquipmentService service;
    private final Gson gson;

    public EquipmentController() {
        this.service = new EquipmentService();
        this.gson = new Gson();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {

            if ("GET".equals(method)) {

                if (path.equals("/api/equipments")
                        || path.equals("/api/equipments/")) {

                    String response = gson.toJson(
                            service.getAllEquipments()
                    );

                    sendResponse(exchange, 200, response);

                } else if (path.startsWith("/api/equipments/")) {

                    String idText = path.substring(
                            "/api/equipments/".length()
                    );

                    Long id = Long.parseLong(idText);

                    Equipment equipment =
                            service.getEquipmentById(id);

                    if (equipment != null) {

                        sendResponse(
                                exchange,
                                200,
                                gson.toJson(equipment)
                        );

                    } else {

                        sendResponse(
                                exchange,
                                404,
                                "{\"error\":\"Equipamento não encontrado\"}"
                        );
                    }

                } else {

                    sendResponse(
                            exchange,
                            404,
                            "{\"error\":\"Rota não encontrada\"}"
                    );
                }

            } else if ("POST".equals(method)) {

                if (!path.equals("/api/equipments")) {
                    sendResponse(
                            exchange,
                            404,
                            "{\"error\":\"Rota não encontrada\"}"
                    );
                    return;
                }

                String requestBody = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );

                Equipment equipment = gson.fromJson(
                        requestBody,
                        Equipment.class
                );

                service.createEquipment(equipment);


sendResponse(
        exchange,
        201,
        "{\"message\":\"Equipamento cadastrado com sucesso\"}"
);

} else if ("PUT".equals(method)) {

    if (!path.matches("/api/equipments/[0-9]+")) {
        sendResponse(
                exchange,
                400,
                "{\"error\":\"Informe um ID válido na URL\"}"
        );
        return;
    }

    Long id = Long.parseLong(
            path.substring("/api/equipments/".length())
    );

    String requestBody = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
    );

    Equipment received = gson.fromJson(
            requestBody,
            Equipment.class
    );

    if (received == null) {
        sendResponse(
                exchange,
                400,
                "{\"error\":\"Dados do equipamento são obrigatórios\"}"
        );
        return;
    }

    Equipment updated = new Equipment(
            id,
            received.getName(),
            received.getModel(),
            received.getManufacturer(),
            received.getManufacturingYear(),
            received.getActive()
    );

    boolean success = service.updateEquipment(updated);


} else if ("DELETE".equals(method)) {

    if (!path.matches("/api/equipments/[0-9]+")) {
        sendResponse(
                exchange,
                400,
                "{\"error\":\"Informe um ID válido na URL\"}"
        );
        return;
    }

    Long id = Long.parseLong(
            path.substring("/api/equipments/".length())
    );

    boolean deleted = service.deleteEquipment(id);

    if (deleted) {
        sendResponse(
                exchange,
                200,
                "{\"message\":\"Equipamento excluído com sucesso\"}"
        );
    } else {
        sendResponse(
                exchange,
                404,
                "{\"error\":\"Equipamento não encontrado\"}"
        );
    }

} else {


                

                exchange.getResponseHeaders().set(
                        "Allow", "GET, POST, PUT, DELETE"
                );

                sendResponse(
                        exchange,
                        405,
                        "{\"error\":\"Método não permitido\"}"
                );
            }

        } catch (NumberFormatException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"ID inválido\"}"
            );

        } catch (IllegalArgumentException e) {

            sendResponse(
                    exchange,
                    400,
                    gson.toJson(Map.of("error", e.getMessage()))
            );

        } catch (JsonParseException e) {

            sendResponse(
                    exchange,
                    400,
                    "{\"error\":\"JSON inválido\"}"
            );
        }
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length
        );

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
