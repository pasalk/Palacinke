import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlantService } from '../../services/plant.service';
import { Plant } from '../../models/plant';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-plant-cards',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './plant-cards.component.html',
  styleUrls: ['./plant-cards.component.css'],
})
export class PlantCardsComponent implements OnInit {
  plants: Plant[] = [];

  constructor(private plantService: PlantService) {}

  ngOnInit(): void {
    this.plantService.getAllPlants().subscribe((plants) => (this.plants = plants));
    this.plantService.plantRefreshSub.subscribe((plants) => (this.plants = plants));
    // this.plants = [
    //   {
    //     id: '1',
    //     name: 'Aloe Vera',
    //     minHumidity: 30,
    //     maxHumidity: 50,
    //     currentHumidity: 40,
    //   },
    //   {
    //     id: '2',
    //     name: 'Spider Plant',
    //     minHumidity: 40,
    //     maxHumidity: 60,
    //     currentHumidity: 45,
    //   },
    // ];
  }
}
