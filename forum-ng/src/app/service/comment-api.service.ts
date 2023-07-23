import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { CommentModel } from '../model/comment-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class CommentApiService {

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //return only comments belonging to post
   getCommentsByPost(categoryId: number, postId: number) {
      return this.http.get<CommentModel[]>(this.buildUrl(categoryId, postId));
   }

   postComment(categoryId: number, postId: number, comment: CommentModel) {
      return this.http.post<CommentModel>(this.buildUrl(categoryId, postId), comment);
   }

   replyToComment(categoryId: number, postId: number, commentIdToReply: number, comment: CommentModel) {
      return this.http.post<CommentModel>(this.buildUrl(categoryId, postId), comment, {
         params: {
            replyTo: commentIdToReply
         }
      });
   }

   putComment(categoryId: number, postId: number, comment: CommentModel) {
      return this.http.put<CommentModel>(this.buildUrl(categoryId, postId, comment.id), comment);
   }

   deleteComment(categoryId: number, postId: number, commentId: number) {
      return this.http.delete<any>(this.buildUrl(categoryId, postId, commentId));
   }

   likeComment(categoryId: number, postId: number, commentId: number, isLike: boolean) {
      return this.http.put<any>(`${this.buildUrl(categoryId, postId, commentId)}/like/${isLike}`, null);
   }

   private buildUrl(categoryId: string | number, postId: string | number, commentId?: string | number) {
      let baseUrl = `${environment.BASE_URL}/categories/${categoryId}/posts/${postId}/comments`;
      return commentId !== undefined ? baseUrl + `/${commentId}` : baseUrl;
   }
}
