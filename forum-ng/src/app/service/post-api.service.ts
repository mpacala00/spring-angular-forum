import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { PostModel } from '../model/post-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class PostApiService {

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //return category objects with posts
   getPostsByCategory(categoryId: number) {
      return this.http.get<PostModel[]>(this.buildUrl(categoryId));
   }

   getPostById(categoryId: number, postId: number) {
      return this.http.get<PostModel>(this.buildUrl(categoryId, postId));
   }

   postPost(categoryId: number, post: PostModel) {
      return this.http.post<PostModel>(this.buildUrl(categoryId), post);
   }

   putPost(categoryId: number, postId: number, post: PostModel) {
      return this.http.put<PostModel>(this.buildUrl(categoryId, postId), post);
   }

   deletePost(categoryId: number, postId: number) {
      return this.http.delete<any>(this.buildUrl(categoryId, postId));
   }

   likeComment(categoryId: number, postId: number, isLike: boolean) {
      return this.http.put<any>(`${this.buildUrl(categoryId, postId)}/like/${isLike}`, null);
   }

   private buildUrl(categoryId: string | number, postId?: string | number) {
      let baseUrl = `${environment.BASE_URL}/categories/${categoryId}/posts`;
      return postId !== undefined ? baseUrl + `/${postId}` : baseUrl;
   }
}
