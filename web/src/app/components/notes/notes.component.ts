import {Component, inject, OnInit} from '@angular/core';
import {Note} from "../../models/Note";
import {NgForOf, NgIf} from "@angular/common";
import {HttpClient} from "@angular/common/http";
import {CreatePostPopupComponent} from "../../shared/components/create-post-popup/create-post-popup.component";
import {NoteStateServiceService} from "../../shared/services/note-state-service.service";

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [
    NgForOf,
    CreatePostPopupComponent,
    NgIf
  ],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css'
})
export class NotesComponent implements OnInit {
  httpClient = inject(HttpClient);

  notes: Note[] = [];

  constructor(public noteStateService: NoteStateServiceService) {}

  ngOnInit(): void {
    this.fetchNotes();
  }

  fetchNotes() {
    this.httpClient.get<Note[]>(`http://localhost:8080/posts`).subscribe((data) => {
      console.log(JSON.stringify(data))
      this.notes = data
    })
  }
}
