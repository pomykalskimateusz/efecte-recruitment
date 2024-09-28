import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NoteStateServiceService {
  private createDialogVisible: boolean = false;

  isCreatePostDialogVisible = this.createDialogVisible;

  showCreatePostDialog() {
    this.isCreatePostDialogVisible = true
  }

  hideCreatePostDialog() {
    this.isCreatePostDialogVisible = false;
  }
}
