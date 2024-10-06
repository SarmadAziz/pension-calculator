import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ParticipantDetailsResponse {
  id: number;
  name: string;
  dateOfBirth: string;
  email: string;
  address: {
    street: string;
    houseNumber: number;
    city: string;
  };
  monthlyPremium: number;
  currentPensionValue: number;
  expectedPensionValue: number;
}

export interface ExpectedPensionValueRequest {
  participantId: number;
  updatedRetirementAge: number;
  currentPensionValue: number;
}

@Injectable({
  providedIn: 'root'
})
export class ParticipantService {

  private apiUrl = 'http://localhost:8080/api/v1/pensiondetails';

  constructor(private http: HttpClient) { }

  fetchParticipantDetails(participantId: number): Observable<ParticipantDetailsResponse> {
    return this.http.get<ParticipantDetailsResponse>(`${this.apiUrl}/${participantId}`);
  }

  recalculatePension(expectedPensionValueRequest: ExpectedPensionValueRequest): Observable<number> {
    return this.http.post<number>(`${this.apiUrl}/recalculate-pension`, expectedPensionValueRequest);
  }
}
