
package com.agrotech.service;

import java.util.List;

import com.agrotech.model.Equipment;
import com.agrotech.repository.EquipmentRepository;

public class EquipmentService {

    private final EquipmentRepository repository;

    public EquipmentService() {
        this.repository = new EquipmentRepository();
    }

    public void createEquipment(Equipment equipment) {

        validateEquipment(equipment);

        repository.save(equipment);
    }

    public List<Equipment> getAllEquipments() {
        return repository.findAll();
    }

    public Equipment getEquipmentById(Long id) {
        return repository.findById(id);
    }

    public boolean updateEquipment(Equipment equipment) {

        validateEquipment(equipment);

        return repository.update(equipment);
    }

    public boolean deleteEquipment(Long id) {
        return repository.deleteById(id);
    }

    private void validateEquipment(Equipment equipment) {

        if (equipment == null) {
            throw new IllegalArgumentException(
                    "Equipamento não pode ser nulo"
            );
        }

        if (equipment.getName() == null ||
                equipment.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Nome do equipamento é obrigatório"
            );
        }

        if (equipment.getModel() == null ||
                equipment.getModel().isBlank()) {

            throw new IllegalArgumentException(
                    "Modelo do equipamento é obrigatório"
            );
        }

        if (equipment.getManufacturer() == null ||
                equipment.getManufacturer().isBlank()) {

            throw new IllegalArgumentException(
                    "Fabricante é obrigatório"
            );
        }

        if (equipment.getManufacturingYear() == null ||
                equipment.getManufacturingYear() < 1900) {

            throw new IllegalArgumentException(
                    "Ano de fabricação inválido"
            );
        }

        if (equipment.getActive() == null) {

            throw new IllegalArgumentException(
                    "Status do equipamento é obrigatório"
            );
        }
    }
}
