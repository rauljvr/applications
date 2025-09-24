import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerDownline } from './player-downline';

describe('PlayerDownline', () => {
  let component: PlayerDownline;
  let fixture: ComponentFixture<PlayerDownline>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerDownline]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerDownline);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
