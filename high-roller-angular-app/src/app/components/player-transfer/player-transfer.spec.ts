import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerTransfer } from './player-transfer';

describe('PlayerTransfer', () => {
  let component: PlayerTransfer;
  let fixture: ComponentFixture<PlayerTransfer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerTransfer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerTransfer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
