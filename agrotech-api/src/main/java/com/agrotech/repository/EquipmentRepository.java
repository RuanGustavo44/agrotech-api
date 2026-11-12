
package com.agrotech.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.agrotech.database.DatabaseConnection;
import com.agrotech.model.Equipment;

public class EquipmentRepository {

    public void save(Equipment equipment) {

        String sql = """
            INSERT INTO equipment
            (name, model, manufacturer, manufacturing_year, active)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, equipment.getName());
            statement.setString(2, equipment.getModel());
            statement.setString(3, equipment.getManufacturer());
            statement.setInt(4, equipment.getManufacturingYear());
            statement.setBoolean(5, equipment.getActive());

            statement.executeUpdate();

            System.out.println("Equipamento cadastrado com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erro ao cadastrar equipamento", e
            );
        }
        
    }

public List<Equipment> findAll() {

    String sql = "SELECT * FROM equipment";

    List<Equipment> equipments = new ArrayList<>();

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql);

         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {

            Equipment equipment = new Equipment(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("model"),
                    resultSet.getString("manufacturer"),
                    resultSet.getInt("manufacturing_year"),
                    resultSet.getBoolean("active")
            );

            equipments.add(equipment);
        }

    } catch (SQLException e) {
        throw new RuntimeException(
                "Erro ao listar equipamentos", e
        );
    }

    return equipments;
}



public Equipment findById(Long id) {

    String sql = "SELECT * FROM equipment WHERE id = ?";

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setLong(1, id);

        try (ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return new Equipment(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("model"),
                        resultSet.getString("manufacturer"),
                        resultSet.getInt("manufacturing_year"),
                        resultSet.getBoolean("active")
                );
            }
        }

    } catch (SQLException e) {
        throw new RuntimeException(
                "Erro ao buscar equipamento", e
        );
    }

    return null;
}

public boolean update(Equipment equipment) {

    String sql = """
        UPDATE equipment
        SET name = ?,
            model = ?,
            manufacturer = ?,
            manufacturing_year = ?,
            active = ?
        WHERE id = ?
        """;

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, equipment.getName());
        statement.setString(2, equipment.getModel());
        statement.setString(3, equipment.getManufacturer());
        statement.setInt(4, equipment.getManufacturingYear());
        statement.setBoolean(5, equipment.getActive());
        statement.setLong(6, equipment.getId());

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;

    } catch (SQLException e) {
        throw new RuntimeException(
                "Erro ao atualizar equipamento", e
        );
    }
}

public boolean deleteById(Long id) {

    String sql = "DELETE FROM equipment WHERE id = ?";

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setLong(1, id);

        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;

    } catch (SQLException e) {
        throw new RuntimeException(
                "Erro ao excluir equipamento", e
        );
    }
}


}


