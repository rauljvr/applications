import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerExit } from './player-exit';

describe('PlayerExit', () => {
  let component: PlayerExit;
  let fixture: ComponentFixture<PlayerExit>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerExit]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerExit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
