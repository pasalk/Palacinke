package com.example.back.Controller;

import com.example.back.Entity.Plant;
import com.example.back.RequestBody.PlantHumidityUpdateRequest;
import com.example.back.RequestBody.PlantRequestBody;
import com.example.back.Service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PlantController {
    private PlantService plantService;

    @Autowired
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping(path = "/addPlant",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Boolean> add(@RequestBody PlantRequestBody request) throws URISyntaxException {
        if (plantService.getPlantById(request.getId()).isEmpty())
            return new ResponseEntity<>(false, HttpStatus.OK);

        Plant plant = new Plant(request.getId(), request.getName(), request.getMinHumidity(), request.getMaxHumidity(), -1);
        plantService.addPlant(plant);
        plantService.sendPlantDataToHA("http://192.168.43.133:8123/plant/data", plant);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/allPlants")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<List<Plant>> getAll() {
        return new ResponseEntity<>(plantService.getAllPlants(), HttpStatus.OK);
    }

    @GetMapping("/humidity/{plantId}")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<Integer> getPlantHumidity(@PathVariable(value="plantId") String id) {
        if (plantService.getPlantById(id).isPresent()) {
            return new ResponseEntity<>(plantService.getPlantHumidity(id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(-1, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(path = "/humidity/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://192.168.184.133:8123")
    public ResponseEntity<Boolean> setPlantHumidity(@RequestBody PlantHumidityUpdateRequest request) throws URISyntaxException {
        if (plantService.getPlantById(request.getDevice()).isEmpty()) {
            Plant plant = new Plant(request.getDevice());
            plantService.addPlant(plant);
        }
            double primitiveDoubleHumidity = Double.parseDouble(request.getHumidity());
            int humidity = (int) primitiveDoubleHumidity;
            plantService.setPlantHumidity(request.getDevice(), humidity);

            Plant plant = plantService.getPlantById(request.getDevice()).get();
            plantService.updateMinMaxHumidity("http://192.168.184.133:8123/api/webhook/update_min_max_humidity", plant);
            return new ResponseEntity<>(true, HttpStatus.OK);

            /*
            if (plant.getCurrentHumidity() < plant.getMinHumidity()) {
                plantService.lowHumidityRequest("http://192.168.184.133:8123/humidity/low", request.getDevice());
                return new ResponseEntity<>(false, HttpStatus.OK);
            }
            else {
                System.out.println(true);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }

             */

    }

    @PostMapping(path = "/registerDevice",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://192.168.184.133:8123")
    public ResponseEntity<Boolean> registerNewDevice(@RequestBody String id) throws URISyntaxException {
        if (plantService.getPlantById(id).isPresent()) return new ResponseEntity<>(false, HttpStatus.OK);

        Plant plant = new Plant(id);
        plantService.addPlant(plant);

        Plant newPlant = plantService.getPlantById(id).get();
        plantService.updateMinMaxHumidity("http://192.168.184.133:8123/api/webhook/update_min_max_humidity", newPlant);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping("/getUnregistered")
    @CrossOrigin(origins = "http://localhost:4200")
    public ResponseEntity<ArrayList<String>> getUnregisteredDevices() {
        ArrayList<String> ret = plantService.getUnregisteredDevices();
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
