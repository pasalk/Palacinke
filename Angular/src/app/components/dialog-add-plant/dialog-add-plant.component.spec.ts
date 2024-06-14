import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogAddPlantComponent } from './dialog-add-plant.component';

describe('DialogAddPlantComponent', () => {
  let component: DialogAddPlantComponent;
  let fixture: ComponentFixture<DialogAddPlantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DialogAddPlantComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogAddPlantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
