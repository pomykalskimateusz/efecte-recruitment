import { Component } from '@angular/core';
import {NoteStateServiceService} from "../../services/note-state-service.service";

@Component({
  selector: 'app-create-post-popup',
  standalone: true,
  imports: [],
  templateUrl: './create-post-popup.component.html',
  styleUrl: './create-post-popup.component.css'
})
export class CreatePostPopupComponent {
  constructor(public noteStateService: NoteStateServiceService) {}
}
