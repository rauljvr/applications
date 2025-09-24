import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reference } from './player-referral';

describe('Reference', () => {
  let component: Reference;
  let fixture: ComponentFixture<Reference>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reference]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reference);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
