import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { PostModel } from '../model/post-model';
import { CategoryModel } from '../model/category-model';
import { CommentModel } from '../model/comment-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class CommentApiService {

   private PUT_COMMENT = `${environment.BASE_URL}/comment`;

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //return only comments belonging to post
   getCommentsByPost(postId: number) {
      return this.http.get<CommentModel[]>(`${environment.BASE_URL_PUBLIC}/post/${postId}/comments`);
   }

   postComment(postId: number, comment: CommentModel) {
      return this.http.post<CommentModel>(`${environment.BASE_URL}/post/${postId}/comment`, comment, { headers: this.headersObj });
   }

   putComment(comment: CommentModel) {
      return this.http.put<CommentModel>(this.PUT_COMMENT, comment, { headers: this.headersObj });
   }

   deleteComment(commentId: number) {
      return this.http.delete<any>(`${environment.BASE_URL}/comment/${commentId}`, { headers: this.headersObj });
   }

   private headersObj = new HttpHeaders({
      'Authorization': "Bearer " + this.cookieService.get('token'),
      'Access-Control-Allow-Origin': 'http://localhost:8080',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type:application/json',
      'Access-Control-Allow-Credentials': 'true'
   });
}
