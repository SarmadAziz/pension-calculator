import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ParticipantService, ParticipantDetailsResponse, ExpectedPensionValueRequest } from './participant.service';

describe('ParticipantService', () => {
  let service: ParticipantService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ParticipantService]
    });

    service = TestBed.inject(ParticipantService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch participant details by ID', () => {
    const mockParticipantDetails: ParticipantDetailsResponse = {
      id: 1,
      name: 'John Doe',
      dateOfBirth: '1964-01-01',
      email: 'a@a.com',
      address: {
        street: 'straat',
        houseNumber: 1,
        city: 'Zaandam'
      },
      monthlyPremium: 150.0,
      currentPensionValue: 100000.00,
      expectedPensionValue: 125000.00
    };

    service.fetchParticipantDetails(1).subscribe((response) => {
      expect(response).toEqual(mockParticipantDetails);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/pensiondetails/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockParticipantDetails);
  });

  it('should recalculate pension based on the request', () => {
    const mockPensionRequest: ExpectedPensionValueRequest = {
      participantId: 1,
      updatedRetirementAge: 65,
      currentPensionValue: 100000.00
    };
    const expectedPensionValue = 120000;

    service.recalculatePension(mockPensionRequest).subscribe((response) => {
      expect(response).toEqual(expectedPensionValue);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/v1/pensiondetails/recalculate-pension');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockPensionRequest);
    req.flush(expectedPensionValue);
  });
});
