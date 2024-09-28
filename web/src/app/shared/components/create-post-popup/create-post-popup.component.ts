import {Component, EventEmitter, inject, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {buildErrorMessage} from "../../utils/http-response.utils";

@Component({
  selector: 'app-create-post-popup',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './create-post-popup.component.html',
  styleUrl: './create-post-popup.component.css'
})
export class CreatePostPopupComponent {
  @Output() postCreatedEvent = new EventEmitter()
  @Output() createPopupCancelledEvent = new EventEmitter()

  httpClient = inject(HttpClient);

  errorMessage: string | null = null
  createPostForm: FormGroup = new FormGroup({
    content: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(200)])
  });

  onPopupCancelled() {
    this.createPopupCancelledEvent.emit()
  }

  onSubmit() {
    this.httpClient.post(`http://localhost:8080/posts`, {content: this.createPostForm.value.content}).subscribe({
      next: (_) => this.postCreatedEvent.emit(),
      error: (error: HttpErrorResponse) => {
        this.errorMessage = buildErrorMessage(error)
      }
    })
  }
}
