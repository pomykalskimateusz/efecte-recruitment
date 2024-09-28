import {Component, OnInit} from '@angular/core';
import {PostsComponent} from "./components/posts/posts.component";
import {HttpClientModule} from "@angular/common/http";
import {CreatePostPopupComponent} from "./components/create-post-popup/create-post-popup.component";
import {EditPostPopupComponent} from "./components/edit-post-popup/edit-post-popup.component";
import {Post,} from "./models/post.model";
import {NgIf} from "@angular/common";
import {ApiService} from "./shared/services/api.service";


@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    HttpClientModule,
    PostsComponent,
    CreatePostPopupComponent,
    EditPostPopupComponent,
    NgIf,
  ],
  providers: [
    ApiService
  ],
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  isCreateDialogVisible: boolean = false
  isEditDialogVisible: boolean = false
  posts: Post[] = []
  editedPost: Post | null = null

  constructor(private apiService: ApiService) {}

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
    this.apiService.fetchPostList((posts) => this.posts = posts)
  }

  private fetchSinglePost(postId: string): void {
    this.apiService.fetchPost(postId, (post) => {
      this.editedPost = post
      this.isEditDialogVisible = true
    })
  }
}
