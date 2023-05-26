import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

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
      return this.http.post<CommentModel>(`${environment.BASE_URL}/post/${postId}/comment`, comment);
   }

   replyToComment(postId: number, commentId: number, comment: CommentModel) {
      return this.http.post<CommentModel>(`${environment.BASE_URL}/posts/${postId}/comment/${commentId}`, comment);
   }

   putComment(comment: CommentModel) {
      return this.http.put<CommentModel>(this.PUT_COMMENT, comment);
   }

   deleteComment(commentId: number) {
      return this.http.delete<any>(`${environment.BASE_URL}/comment/${commentId}`);
   }

   likeComment(commentId: number, isLike: boolean) {
      return this.http.put<any>(`${environment.BASE_URL}/comments/${commentId}/like/${isLike}`, null);
   }
}
