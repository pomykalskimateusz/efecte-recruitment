import { Component } from '@angular/core';
import {NotesComponent} from "./components/notes/notes.component";
import {HttpClientModule} from "@angular/common/http";


@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    HttpClientModule,
    NotesComponent,
  ],
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Efecte - Post It';
}
