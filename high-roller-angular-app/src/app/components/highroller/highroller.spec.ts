import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Highroller } from './highroller';

describe('Highroller', () => {
  let component: Highroller;
  let fixture: ComponentFixture<Highroller>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Highroller]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Highroller);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
