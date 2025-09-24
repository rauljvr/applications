import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerName } from './player-name';

describe('PlayerName', () => {
  let component: PlayerName;
  let fixture: ComponentFixture<PlayerName>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerName]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerName);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
