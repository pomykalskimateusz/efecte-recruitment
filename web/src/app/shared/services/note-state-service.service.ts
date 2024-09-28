import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from "rxjs";
import * as uuid from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class NoteStateServiceService {
  private createDialogVisible: boolean = false;
  private editDialogVisible: boolean = false;

  private refreshNoteListSubject = new BehaviorSubject(uuid.v4());
  currentRefreshNoteState = this.refreshNoteListSubject.asObservable();
  refreshNotes = () => {
    this.refreshNoteListSubject.next(uuid.v4())
  }

  private postIdSubject = new BehaviorSubject<string | null>(null);
  currentPostId = this.postIdSubject.asObservable();

  isCreatePostDialogVisible = this.createDialogVisible;
  isEditPostDialogVisible = this.editDialogVisible;

  showCreatePostDialog() {
    this.isCreatePostDialogVisible = true
  }

  showEditPostDialog(postId: string) {
    console.log(`Clicking post: ${postId}`)
    this.postIdSubject.next(postId)
    this.isEditPostDialogVisible = true;
  }

  hideCreatePostDialog() {
    this.isCreatePostDialogVisible = false;
  }

  hideEditPostDialog() {
    this.postIdSubject.next(null)
    this.isEditPostDialogVisible = false;
  }
}
