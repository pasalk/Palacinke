import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { DialogAddPlantComponent } from '../dialog-add-plant/dialog-add-plant.component';
import { PlantService } from '../../services/plant.service';
import { PlantRequestBody } from '../../models/plantRequestBody';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatIconModule,
    MatButtonModule,
    MatDialogModule,
    MatButton,
    MatDividerModule,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
})
export class HeaderComponent {
  
  constructor(private dialog: MatDialog, private plantService: PlantService) {}

  addNewPlant() {
    const dialogRef = this.dialog.open(DialogAddPlantComponent, {
      height: '30rem',
      width: '35rem',
    });
    dialogRef.afterClosed().subscribe((plantReq: PlantRequestBody) => {
      if (plantReq) {
        this.plantService.addPlant(plantReq).subscribe();
      }
    });
  }

  refreshHumidity() {
    this.plantService.refreshAllPlants();
  }
}
