package com.example.back.Service;

import com.example.back.Entity.Plant;
import com.example.back.Repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlantService {
    private PlantRepository plantRepository;

    private final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiIzMWFjZGUyOTc0NTY0MzI5OWIyZDYxZjZiMDBiMGM1ZiIsImlhdCI6MTcxNzY3MTg0MywiZXhwIjoyMDMzMDMxODQzfQ.eW3_xGKQ8VSYcmj0ur9iUykT8ml_BTiqkEtr_6jz2Ls";

    @Autowired
    public PlantService(PlantRepository plantRepository) {this.plantRepository = plantRepository;}

    public Optional<Plant> getPlantById(String id) {
        return plantRepository.findById(id);
    }

    public void addPlant(Plant plant) {
        plantRepository.deleteById(plant.getId());
        plantRepository.save(plant);
    }

    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    public Integer getPlantHumidity(String id) {
        return plantRepository.findById(id).get().getCurrentHumidity();
    }

    public void setPlantHumidity(String id, int humidity) {
        Plant oldPlant = plantRepository.findById(id).get();
        Plant updatePlant = new Plant(oldPlant.getId(), oldPlant.getName(), 40, 60, humidity);
        plantRepository.delete(oldPlant);
        plantRepository.save(updatePlant);
    }

    public void lowHumidityRequest(String url, String device) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        String body = "{\n" +
                "    \"device\" : \"" + device + "\",\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        URI uri = new URI(url);

        String response = restTemplate.postForObject(uri, request, String.class);
        System.out.println(response);
    }

    public void updateMinMaxHumidity(String url, Plant plant) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + BEARER_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        String body = "{\n" +
                "\"device_id\" : \"" + plant.getId() + "\",\n" +
                "\"minHumidity\": \"" + plant.getMinHumidity() + "\",\n" +
                "\"maxHumidity\": \"" + plant.getMaxHumidity() + "\"\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        URI uri = new URI(url);
        restTemplate.postForObject(uri, request, String.class);
    }

    public ArrayList<String> getUnregisteredDevices() {
        ArrayList<String> ret = new ArrayList<>();
        List<Plant> allPlants = getAllPlants();
        for (Plant plant : allPlants) {
            if (plant.getName().isBlank() || plant.getMinHumidity() == 100 || plant.getMaxHumidity() == 0)
                ret.add(plant.getId());
        }
        return ret;
    }

    public void sendPlantDataToHA(String url, Plant plant) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        String body = "{\n" +
                "    \"device_id\" : \"" + plant.getId() + "\",\n" +
                "    \"name\" : \"" + plant.getName() + "\",\n" +
                "    \"minHumidity\" : " + plant.getMinHumidity() + ",\n" +
                "    \"maxHumidity\" : " + plant.getMaxHumidity() + "\n" +
                "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        URI uri = new URI(url);

        String response = restTemplate.postForObject(uri, request, String.class);
        System.out.println(response);
    }
}
