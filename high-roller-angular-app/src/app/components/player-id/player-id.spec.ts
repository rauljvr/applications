import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerId } from './player-id';

describe('PlayerId', () => {
  let component: PlayerId;
  let fixture: ComponentFixture<PlayerId>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlayerId]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerId);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
