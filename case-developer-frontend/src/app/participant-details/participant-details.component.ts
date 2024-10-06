import { Component, OnInit } from '@angular/core';
import { ParticipantService, ParticipantDetailsResponse, ExpectedPensionValueRequest } from '../services/participant.service';
import { ActivatedRoute } from '@angular/router';
import {FormsModule} from "@angular/forms";
import {NgIf} from "@angular/common";
import {catchError, of, tap} from "rxjs";

@Component({
  selector: 'app-participant-details',
  templateUrl: './participant-details.component.html',
  styleUrls: ['./participant-details.component.css'],
  imports: [
    FormsModule,
    NgIf
  ],
  standalone: true
})
export class ParticipantDetailsComponent implements OnInit {
  participantId!: number;
  participantDetails!: ParticipantDetailsResponse;
  newRetirementAge!: number;
  expectedPensionValue!: number;
  hasCorrectRetirementAge: boolean = true;

  constructor(
    private participantService: ParticipantService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.participantId = Number(this.route.snapshot.paramMap.get('id'));
    this.fetchParticipantDetails();
  }

  fetchParticipantDetails(): void {
    this.participantService.fetchParticipantDetails(this.participantId).pipe(
      tap(data => {
        this.participantDetails = data;
      }),
      catchError(error => {
        return of(null);
      })
    ).subscribe();
  }

  getCurrentAge(): number {
    const birthDate = new Date(this.participantDetails.dateOfBirth);
    const today = new Date();
    return today.getFullYear() - birthDate.getFullYear();
  }

  recalculatePension(): void {
    this.hasCorrectRetirementAge = this.newRetirementAge > this.getCurrentAge();

    if (this.newRetirementAge && this.hasCorrectRetirementAge) {
      const request: ExpectedPensionValueRequest = {
        participantId: this.participantId,
        currentPensionValue: this.participantDetails.currentPensionValue,
        updatedRetirementAge: this.newRetirementAge,
      };

      this.participantService.recalculatePension(request).pipe(
        tap(value => {
          this.expectedPensionValue = value;
        }),
        catchError(error => {
          return of(null);
        })
      ).subscribe();
    }
  }
}
