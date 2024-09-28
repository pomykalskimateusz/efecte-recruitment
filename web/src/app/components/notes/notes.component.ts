import {Component, inject, OnInit} from '@angular/core';
import {Note} from "../../models/Note";
import {NgForOf} from "@angular/common";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [
    NgForOf
  ],
  templateUrl: './notes.component.html',
  styleUrl: './notes.component.css'
})
export class NotesComponent implements OnInit {
  httpClient = inject(HttpClient);

  notes: Note[] = [];

  constructor() {
  }

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
