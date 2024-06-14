import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import {
  ReactiveFormsModule,
  FormGroup,
  FormBuilder,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { PlantRequestBody } from '../../models/plantRequestBody'; // Adjust the path as needed
import { PlantService } from '../../services/plant.service';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-dialog-add-plant',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
  ],
  templateUrl: './dialog-add-plant.component.html',
  styleUrls: ['./dialog-add-plant.component.css'],
})
export class DialogAddPlantComponent implements OnInit {
  plantForm: FormGroup;
  unregisteredDevices: string[] = [];
  noDevicesMessage: string = "No available devices to register.";

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DialogAddPlantComponent>,
    private plantService: PlantService
  ) {
    this.plantForm = this.fb.group({
      id: ['', Validators.required],
      name: ['', Validators.required],
      minHumidity: [0, [Validators.required, Validators.min(0)]],
      maxHumidity: [0, [Validators.required, Validators.min(0)]],
    });
  }
  ngOnInit(): void {
    this.loadUnregisteredDevices();
  }

  loadUnregisteredDevices(): void {
    this.plantService.getUnregisteredDevices().subscribe((devices) => {
      this.unregisteredDevices = devices.filter(device => device !== '-1');
    });
  }

  onSubmit(): void {
    if (this.plantForm.valid) {
      const plantRequest: PlantRequestBody = this.plantForm.value;
      this.dialogRef.close(plantRequest);
    }
  }

  onCancel(): void {
    this.dialogRef.close(null);
  }
}
