import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import * as uuid from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class NoteStateServiceService {
  private createDialogVisible: boolean = false;
  private refreshNoteListSubject = new BehaviorSubject(uuid.v4());

  currentRefreshNoteState = this.refreshNoteListSubject.asObservable();

  refreshNotes = () => {
    this.refreshNoteListSubject.next(uuid.v4())
  }

  isCreatePostDialogVisible = this.createDialogVisible;

  showCreatePostDialog() {
    this.isCreatePostDialogVisible = true
  }

  hideCreatePostDialog() {
    this.isCreatePostDialogVisible = false;
  }
}
