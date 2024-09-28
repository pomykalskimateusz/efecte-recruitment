import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Post} from "../../models/post.model";
import {NgForOf, NgIf} from "@angular/common";
import {CreatePostPopupComponent} from "../create-post-popup/create-post-popup.component";
import {EditPostPopupComponent} from "../edit-post-popup/edit-post-popup.component";

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
  @Output() editPostDialogOpenedEvent = new EventEmitter<string>()


  createPostDialogOpened() {
    this.createPostDialogOpenedEvent.emit();
  }

  editPostDialogOpened(postId: string) {
    this.editPostDialogOpenedEvent.emit(postId)
  }
}
