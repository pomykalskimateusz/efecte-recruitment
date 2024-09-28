import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {ApiService} from "../../services/api.service";

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

  constructor(private apiService: ApiService) {}

  errorMessage: string | null = null
  createPostForm: FormGroup = new FormGroup({
    content: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(200)])
  });

  onPopupCancelled() {
    this.createPopupCancelledEvent.emit()
  }

  onSubmit() {
    this.apiService.savePost(
      this.createPostForm.value.content,
      () => this.postCreatedEvent.emit(),
      (error) => this.errorMessage = error
      )
  }
}
