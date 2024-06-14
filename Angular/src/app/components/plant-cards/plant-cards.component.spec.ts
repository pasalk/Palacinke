import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlantCardsComponent } from './plant-cards.component';

describe('PlantCardsComponent', () => {
  let component: PlantCardsComponent;
  let fixture: ComponentFixture<PlantCardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlantCardsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlantCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
