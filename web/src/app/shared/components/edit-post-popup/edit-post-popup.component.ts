import {Component, inject, OnInit} from '@angular/core';
import {NoteStateServiceService} from "../../services/note-state-service.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {NgIf} from "@angular/common";
import {Note} from "../../../models/Note";

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
  httpClient = inject(HttpClient);
  errorMessage: string | null = null
  post: Note | null = null;

  createPostForm: FormGroup = new FormGroup({
    content: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(200)])
  });

  constructor(public noteStateService: NoteStateServiceService) {}

  ngOnInit() {
    this.noteStateService.currentPostId.subscribe((postId) => {
        if(postId !== null && this.post === null) {
          this.httpClient.get<Note>(`http://localhost:8080/posts/${postId}`).subscribe(data => {
            this.post = data
            this.createPostForm.get('content')?.setValue(data.content)
          })
        }
    })
  }

  cancelEditingPopup() {
    this.post = null;
  }

  deletePost() {
    this.httpClient.delete(`http://localhost:8080/posts/${this.post?.id}?version=${this.post?.version}`)
      .subscribe((result) => {
        this.post = null;
        this.noteStateService.refreshNotes()
        this.noteStateService.hideEditPostDialog();
      }, (error: HttpErrorResponse) => {
        if(error.status === 500) {
          this.errorMessage = 'Something went wrong. Try again.'
        }
      })
  }

  onSubmit() {
    console.log(this.createPostForm.value);
    this.httpClient.put(`http://localhost:8080/posts/${this.post?.id}`, {content: this.createPostForm.value.content, version: this.post?.version})
      .subscribe((result) => {
        this.post = null;
        this.noteStateService.refreshNotes()
        this.noteStateService.hideEditPostDialog();
    }, (error: HttpErrorResponse) => {
        if(error.status === 500) {
          this.errorMessage = 'Something went wrong. Try again.'
        }
      })
  }
}
