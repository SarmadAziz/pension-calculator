import { TestBed } from '@angular/core/testing';
import { ParticipantDetailsComponent } from './participant-details.component';
import { ParticipantService, ParticipantDetailsResponse, ExpectedPensionValueRequest } from '../services/participant.service';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

class MockParticipantService {
  fetchParticipantDetails(participantId: number) {
    return of(mockParticipantDetails);
  }

  recalculatePension(request: ExpectedPensionValueRequest) {
    return of(13000);
  }
}

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

describe('ParticipantDetailsComponent', () => {
  let component: ParticipantDetailsComponent;
  let mockService: MockParticipantService;
  let fixture: any;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        ParticipantDetailsComponent,
        HttpClientTestingModule,
        FormsModule,
      ],
      providers: [
        { provide: ParticipantService, useClass: MockParticipantService },
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: (key: string) => '1' } } }
        },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ParticipantDetailsComponent);
    component = fixture.componentInstance;
    mockService = TestBed.inject(ParticipantService);
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch participant details on init', () => {
    spyOn(mockService, 'fetchParticipantDetails').and.callThrough();
    component.ngOnInit();
    expect(mockService.fetchParticipantDetails).toHaveBeenCalledWith(1);
    expect(component.participantDetails).toEqual(mockParticipantDetails);
  });

  it('should calculate the correct current age', () => {
    component.participantDetails = mockParticipantDetails;
    expect(component.getCurrentAge()).toBe(60);
  });

  it('should not recalculate pension if retirement age is less than current age', () => {
    component.participantDetails = mockParticipantDetails;
    component.newRetirementAge = component.getCurrentAge() - 1;
    component.recalculatePension();

    expect(component.hasCorrectRetirementAge).toBeFalse();
    expect(component.expectedPensionValue).toBeUndefined();
  });

  it('should recalculate pension if retirement age is valid', () => {
    component.participantDetails = mockParticipantDetails;
    component.newRetirementAge = component.getCurrentAge() + 1;
    spyOn(mockService, 'recalculatePension').and.callThrough();

    component.recalculatePension();

    expect(component.hasCorrectRetirementAge).toBeTrue();
    expect(mockService.recalculatePension).toHaveBeenCalledWith({
      participantId: component.participantId,
      currentPensionValue: component.participantDetails.currentPensionValue,
      updatedRetirementAge: component.newRetirementAge
    });
    expect(component.expectedPensionValue).toBe(13000);
  });
});
