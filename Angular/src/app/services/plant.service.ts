import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, Subject, catchError, of } from 'rxjs';
import { Plant } from '../models/plant';
import { PlantRequestBody } from '../models/plantRequestBody';

@Injectable({
  providedIn: 'root',
})
export class PlantService {
  private apiUrl = 'http://localhost:8080'; // Replace with your actual backend API URL
  public plantRefreshSub = new Subject<Plant[]>();

  constructor(private http: HttpClient) {}
  
  addPlant(request: PlantRequestBody): Observable<boolean> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http
      .post<boolean>(`${this.apiUrl}/addPlant`, request, { headers })
      .pipe(catchError(this.handleError<boolean>('addPlant', false)));
  }

  getAllPlants(): Observable<Plant[]> {
    return this.http
      .get<Plant[]>(`${this.apiUrl}/allPlants`)
      .pipe(catchError(this.handleError<Plant[]>('getAllPlants', [])));
  }

  refreshAllPlants(): void {
    this.http
      .get<Plant[]>(`${this.apiUrl}/allPlants`)
      .pipe(catchError(this.handleError<Plant[]>('getAllPlants', []))).subscribe( (plants) => {
        this.plantRefreshSub.next(plants);
      });
  }

  getPlantHumidity(id: string): Observable<number> {
    return this.http
      .get<number>(`${this.apiUrl}/humidity/${id}`)
      .pipe(catchError(this.handleError<number>('getPlantHumidity', -1)));
  }

  getUnregisteredDevices(): Observable<string[]> {
    return this.http
      .get<string[]>(`${this.apiUrl}/getUnregistered`)
      .pipe(catchError(this.handleError<string[]>('getUnregisteredDevices', ["-1"])));
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
