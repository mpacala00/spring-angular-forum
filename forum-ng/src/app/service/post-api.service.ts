import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { PostModel } from '../model/post-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class PostApiService {

   private GET_POSTS = `${environment.BASE_URL_PUBLIC}/posts`;
   private PUT_POST = `${environment.BASE_URL}/post`;

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   getAllPosts() {
      return this.http.get<PostModel[]>(this.GET_POSTS);
   }

   //return category objects with posts
   getPostsByCategory(categoryId: number) {
      return this.http.get<PostModel[]>(`${environment.BASE_URL_PUBLIC}/category/${categoryId}/posts`);
   }

   //return post object with comments
   getPostComments(postId: number) {
      return this.http.get<PostModel>(`${environment.BASE_URL_PUBLIC}/post/${postId}`);
   }

   postPost(categoryId: number, post: PostModel) {
      return this.http.post<PostModel>(`${environment.BASE_URL}/category/${categoryId}/post`, post);
   }

   putPost(post: PostModel) {
      return this.http.put<PostModel>(this.PUT_POST, post);
   }

   deletePost(postId: number) {
      return this.http.delete<any>(`${environment.BASE_URL}/post/${postId}`);
   }
}
