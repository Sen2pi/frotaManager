import { TestBed } from '@angular/core/testing';

import { Maintenance } from './maintenance';

describe('Maintenance', () => {
  let service: Maintenance;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Maintenance);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
