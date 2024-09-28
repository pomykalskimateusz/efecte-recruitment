import {Component, EventEmitter, inject, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {Post} from "../../../models/post.model";
import {buildErrorMessage} from "../../utils/http-response.utils";

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

  httpClient = inject(HttpClient);

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
    this.httpClient.delete(`http://localhost:8080/posts/${this.post?.id}?version=${this.post?.version}`).subscribe({
      next: (_) => this.postDeletedEvent.emit(),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = buildErrorMessage(error)
      }
    })
  }

  onSubmit() {
    const request = {
      content: this.createPostForm.value.content,
      version: this.post?.version
    }
    this.httpClient.put(`http://localhost:8080/posts/${this.post?.id}`, request).subscribe({
      next: (_) => this.postUpdatedEvent.emit(),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = buildErrorMessage(error)
      }
    })
  }
}
