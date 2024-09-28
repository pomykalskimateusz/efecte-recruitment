import { Component } from '@angular/core';
import {NotesComponent} from "./components/notes/notes.component";
import {HttpClientModule} from "@angular/common/http";
import {CreatePostPopupComponent} from "./shared/components/create-post-popup/create-post-popup.component";
import {EditPostPopupComponent} from "./shared/components/edit-post-popup/edit-post-popup.component";


@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    HttpClientModule,
    NotesComponent,
    CreatePostPopupComponent,
    EditPostPopupComponent
  ],
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Efecte - Post It';
}
