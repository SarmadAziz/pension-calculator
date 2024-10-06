import {Routes} from '@angular/router';
import {ParticipantDetailsComponent} from "./participant-details/participant-details.component";

export const routes: Routes = [
  { path: 'participant/:id', component: ParticipantDetailsComponent },
  { path: '', redirectTo: '/participant/1', pathMatch: 'full' }
];
