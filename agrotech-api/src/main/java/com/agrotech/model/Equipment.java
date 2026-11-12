
package com.agrotech.model;

public class Equipment {

    private Long id;
    private String name;
    private String model;
    private String manufacturer;
    private Integer manufacturingYear;
    private Boolean active;

    public Equipment(Long id, String name, String model,
                     String manufacturer, Integer manufacturingYear,
                     Boolean active) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.manufacturingYear = manufacturingYear;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public Integer getManufacturingYear() {
        return manufacturingYear;
    }

    public Boolean getActive() {
        return active;
    }
}
