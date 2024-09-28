import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {Post} from "../../models/post.model";
import {ApiService} from "../../shared/services/api.service";

@Component({
  selector: 'app-edit-post-popup',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './edit-post-popup.component.html',
  styleUrl: './edit-post-popup.component.css'
})
export class EditPostPopupComponent implements OnInit {
  @Input() post: Post | null = null
  @Output() editPopupCancelledEvent = new EventEmitter()
  @Output() postDeletedEvent = new EventEmitter()
  @Output() postUpdatedEvent = new EventEmitter()

  constructor(private apiService: ApiService) {}

  errorMessage: string | null = null
  createPostForm: FormGroup = new FormGroup({
    content: new FormControl(this.post?.content, [Validators.required, Validators.minLength(1), Validators.maxLength(200)])
  });

  ngOnInit() {
    this.createPostForm.get('content')?.setValue(this.post!.content)
  }

  onPopupCancelled() {
    this.editPopupCancelledEvent.emit()
  }

  deletePost() {
    this.apiService.deletePost(
      this.post!.id,
      this.post!.version,
      () => this.postDeletedEvent.emit(),
      (error) => this.errorMessage = error
    )
  }

  onSubmit() {
    this.apiService.updatePost(
      this.post!.id,
      this.createPostForm.value.content,
      this.post!.version,
      () => this.postUpdatedEvent.emit(),
      (error) => this.errorMessage = error
    )
  }
}
