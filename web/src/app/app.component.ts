import {Component, inject, OnInit, ViewChild} from '@angular/core';
import {NotesComponent} from "./components/notes/notes.component";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {CreatePostPopupComponent} from "./shared/components/create-post-popup/create-post-popup.component";
import {EditPostPopupComponent} from "./shared/components/edit-post-popup/edit-post-popup.component";
import {Post,} from "./models/post.model";
import {NgIf} from "@angular/common";


@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    HttpClientModule,
    NotesComponent,
    CreatePostPopupComponent,
    EditPostPopupComponent,
    NgIf
  ],
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  isCreateDialogVisible: boolean = false
  isEditDialogVisible: boolean = false
  posts: Post[] = []
  editedPost: Post | null = null

  httpClient = inject(HttpClient)

  showCreatePostDialog() {
    this.isCreateDialogVisible = true
  }

  closeCreatePostDialog() {
    this.isCreateDialogVisible = false
  }

  showEditPostDialog(postId: string) {
    this.fetchSinglePost(postId)
  }

  closeEditPostDialog() {
    this.editedPost = null
    this.isEditDialogVisible = false
  }

  handlePostCreated() {
    this.isCreateDialogVisible = false
    this.fetchPosts()
  }

  handlePostModified() {
    this.isEditDialogVisible = false
    this.fetchPosts()
  }

  ngOnInit(): void {
    this.fetchPosts()
  }

  private fetchPosts(): void {
    this.httpClient
      .get<Post[]>(`http://localhost:8080/posts`)
      .subscribe((data) => this.posts = data)
  }

  private fetchSinglePost(postId: string): void {
    this.httpClient.get<Post>(`http://localhost:8080/posts/${postId}`).subscribe(data => {
      this.editedPost = data
      this.isEditDialogVisible = true
    })
  }
}
