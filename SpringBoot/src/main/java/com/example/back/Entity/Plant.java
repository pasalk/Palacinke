package com.example.back.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Plant {
    @Id
    private String id;
    private String name = "";
    private int minHumidity = 100;
    private int maxHumidity = 0;
    private int currentHumidity = -1;

    public Plant(String id) {
        this.id = id;
    }

    public Plant(String id, String name, int currentHumidity) {
        this.id = id;
        this.name = name;
        this.currentHumidity = currentHumidity;
    }
}
