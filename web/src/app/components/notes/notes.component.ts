import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {Post} from "../../models/post.model";
import {NgForOf, NgIf} from "@angular/common";
import {CreatePostPopupComponent} from "../../shared/components/create-post-popup/create-post-popup.component";
import {NoteStateServiceService} from "../../shared/services/note-state-service.service";
import {EditPostPopupComponent} from "../../shared/components/edit-post-popup/edit-post-popup.component";

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [
    NgForOf,
    CreatePostPopupComponent,
    NgIf,
    EditPostPopupComponent
  ],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css'
})
export class NotesComponent {
  @Input() posts: Post[] = [];
  @Output() createPostDialogOpenedEvent = new EventEmitter<string>();

  createPostDialogOpened() {
    this.createPostDialogOpenedEvent.emit();
  }

  constructor(public noteStateService: NoteStateServiceService) {}
}
