import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Post} from "../../models/post.model";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private httpClient: HttpClient) {}

  savePost(content: string, handleSuccess: () => void, handleError: (errorMessage: string) => void) {
    this.httpClient
      .post(`http://localhost:8080/posts`, {content: content})
      .subscribe({
        next: (_) => handleSuccess(),
        error: (error: HttpErrorResponse) => handleError(this.buildErrorMessage(error))
    })
  }

  updatePost(postId: string, content: string, version: number, handleSuccess: () => void, handleError: (errorMessage: string) => void) {
    const request = {
      content: content,
      version: version
    }
    this.httpClient
      .put(`http://localhost:8080/posts/${postId}`, request)
      .subscribe({
        next: (_) => handleSuccess(),
        error: (error: HttpErrorResponse) => handleError(this.buildErrorMessage(error))
    })
  }

  deletePost(postId: string, postVersion: number, handleSuccess: () => void, handleError: (errorMessage: string) => void) {
    this.httpClient
      .delete(`http://localhost:8080/posts/${postId}?version=${postVersion}`)
      .subscribe({
        next: (_) => handleSuccess(),
        error: (error: HttpErrorResponse) => handleError(this.buildErrorMessage(error))
    })
  }

  fetchPost(postId: string, handleResponse: (post: Post) => void) {
    this.httpClient
      .get<Post>(`http://localhost:8080/posts/${postId}`)
      .subscribe(data => handleResponse(data))
  }

  fetchPostList(handleResponse: (postList: Post[]) => void) {
    this.httpClient
      .get<Post[]>(`http://localhost:8080/posts`)
      .subscribe((data) => handleResponse(data))
  }

  buildErrorMessage = (error: HttpErrorResponse) => {
    if(error.status === 404) return 'Resource not found'
    if(error.status === 409) return 'Outdated post version, refresh and try again'
    if(error.status === 400) return 'Incorrect input data, content length should be in range 1-200'
    return 'Something went wrong. Please try again.'
  }
}
