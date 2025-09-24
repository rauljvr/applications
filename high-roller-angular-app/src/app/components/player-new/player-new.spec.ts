import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerNew } from './player-new';

describe('PlayerNew', () => {
  let component: PlayerNew;
  let fixture: ComponentFixture<PlayerNew>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerNew]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerNew);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
