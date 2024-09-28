import {Component, inject, OnInit} from '@angular/core';
import {NoteStateServiceService} from "../../services/note-state-service.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Note} from "../../../models/Note";

@Component({
  selector: 'app-create-post-popup',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './create-post-popup.component.html',
  styleUrl: './create-post-popup.component.css'
})
export class CreatePostPopupComponent implements OnInit {
  httpClient = inject(HttpClient);

  createPostForm: FormGroup = new FormGroup({
    content: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(200)])
  });

  constructor(public noteStateService: NoteStateServiceService) {}

  ngOnInit() {}

  onSubmit() {
    console.log(this.createPostForm.value);
    this.httpClient.post(`http://localhost:8080/posts`, {content: this.createPostForm.value.content})
      .subscribe((_) => {
        this.noteStateService.hideCreatePostDialog();
    })
  }
}
