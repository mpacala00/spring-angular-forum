import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { PostModel } from '../model/post-model';
import { Observable } from 'rxjs';
import { CategoryModel } from '../model/category-model';
import { CommentModel } from '../model/comment-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';
import { UserModel } from '../model/user-model';

@Injectable({
   providedIn: 'root'
})
export class ApiService {

   //todo split this service into several smaller ones
   //todo use http interceptor to put token in headers

   private GET_POSTS = `${environment.BASE_URL_PUBLIC}/posts`;
   private GET_CATEGORIES = `${environment.BASE_URL_PUBLIC}/categories`;
   private GET_USER_DTO = `${environment.BASE_URL}/user`;
   private PUT_POST = `${environment.BASE_URL}/post`;
   private PUT_COMMENT = `${environment.BASE_URL}/comment`;

   private getDeletePostByIdUrl(id: number): string {
      return `${environment.BASE_URL}/post/${id}`;
   }

   private getDeleteCommentByIdUrl(id: number): string {
      return `${environment.BASE_URL}/comment/${id}`;
   }

   private getPostsByCategoryUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/category/${id}/posts`;
   }

   private getPublishPostOnCategoryUrl(id: number): string {
      return `${environment.BASE_URL}/category/${id}/post`;
   }

   private getPostCommentsUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/post/${id}`;
   }

   private getCommentsByPostUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/post/${id}/comments`;
   }

   private getCategoryByIdUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/category/${id}`;
   }

   private getPostCommentUrl(id: number): string {
      return `${environment.BASE_URL}/post/${id}/comment`;
   }

   //user
   private getPostsByUsernameUrl(username: string): string {
      return `${environment.BASE_URL_PUBLIC}/user/${username}/posts`;
   }

   private getCommentsByUsernameUrl(username: string): string {
      return `${environment.BASE_URL_PUBLIC}/user/${username}/comments`;
   }

   private getFollowedCategoriesByUsernameUrl(username: string): string {
      return `${environment.BASE_URL_PUBLIC}/user/${username}/followed-categories`;
   }

   //category is required to publish a post
   //private POST_POST_URL = `${environment.BASE_URL}\\post`;


   //private AUTH_HEADER_CONTENT = "Bearer: " + this.localStorage.retrieve('token');

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   getAllPosts() {
      return this.http.get<PostModel[]>(this.GET_POSTS);
   }

   //todo change cat entity to include description
   getAllCategories() {
      return this.http.get<CategoryModel[]>(this.GET_CATEGORIES);
   }

   getCategoryById(categoryId: number) {
      return this.http.get<CategoryModel>(this.getCategoryByIdUrl(categoryId));
   }

   //just return category, posts will be nested in it...
   getPostsByCategory(categoryId: number) {
      return this.http.get<PostModel[]>(this.getPostsByCategoryUrl(categoryId));
   }

   //return post object with comments
   getPostComments(postId: number) {
      return this.http.get<PostModel>(this.getPostCommentsUrl(postId));
   }

   //return only comments belonging to post
   getCommentsByPost(postId: number) {
      return this.http.get<CommentModel[]>(this.getCommentsByPostUrl(postId));
   }

   postComment(postId: number, comment: CommentModel) {
      return this.http.post<CommentModel>(this.getPostCommentUrl(postId), comment, { headers: this.headersObj });
   }

   putComment(comment: CommentModel) {
      return this.http.put<CommentModel>(this.PUT_COMMENT, comment, { headers: this.headersObj });
   }

   postPost(categoryId: number, post: PostModel) {
      return this.http.post<PostModel>(this.getPublishPostOnCategoryUrl(categoryId), post, { headers: this.headersObj });
   }

   putPost(post: PostModel) {
      return this.http.put<PostModel>(this.PUT_POST, post, { headers: this.headersObj });
   }

   deletePost(postId: number) {
      return this.http.delete<any>(this.getDeletePostByIdUrl(postId), { headers: this.headersObj });
   }

   deleteComment(commentId: number) {
      return this.http.delete<any>(this.getDeleteCommentByIdUrl(commentId), { headers: this.headersObj });
   }

   //USER
   getCurrentUser() {
      return this.http.get<UserModel>(this.GET_USER_DTO + "/current", { headers: this.headersObj });
   }

   getUserByUsername(username: string) {
      return this.http.get<UserModel>(`${environment.BASE_URL}/user/${username}`);
   }

   getPostsByUsername(username: string) {
      return this.http.get<PostModel[]>(this.getPostsByUsernameUrl(username));
   }

   getCommentsByUsername(username: string) {
      return this.http.get<CommentModel[]>(this.getCommentsByUsernameUrl(username));
   }

   getFollowedCategoriesByUsername(username: string) {
      return this.http.get<CategoryModel[]>(this.getFollowedCategoriesByUsernameUrl(username));
   }

   private headersObj = new HttpHeaders({
      'Authorization': "Bearer " + this.cookieService.get('token'),
      'Access-Control-Allow-Origin': 'http://localhost:8080',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type:application/json',
      'Access-Control-Allow-Credentials': 'true'
   });

   private httpOptions = {
      headers: this.headersObj
   };

   createNewPost(post: PostModel): Observable<any> {

      // headers.append("Authorization", "Bearer " + this.localStorage.retrieve('token'));
      // headers.append('Access-Control-Allow-Origin', 'http://localhost:8080');
      // headers.append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');
      // headers.append('Access-Control-Allow-Headers', 'X-Requested-With,content-type');
      // headers.append('Access-Control-Allow-Credentials', 'true');

      console.log('CREATE NEW POST NOT IMPLEMENTED');
      //return this.http.post<Post>(this.POST_POST_URL, post, this.httpOptions);
      return null;
   }

}
