import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Post} from "../../models/post.model";
import {NgForOf, NgIf} from "@angular/common";
import {CreatePostPopupComponent} from "../create-post-popup/create-post-popup.component";
import {EditPostPopupComponent} from "../edit-post-popup/edit-post-popup.component";

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    NgForOf,
    CreatePostPopupComponent,
    NgIf,
    EditPostPopupComponent
  ],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.css'
})
export class PostsComponent {
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
