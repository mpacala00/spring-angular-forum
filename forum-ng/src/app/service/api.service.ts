import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Post } from '../model/post';
import { Observable } from 'rxjs';
import { CategoryModel } from '../model/category-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

@Injectable({
   providedIn: 'root'
})
export class ApiService {

   private GET_POSTS = `${environment.BASE_URL_PUBLIC}/posts`;
   private GET_CATEGORIES = `${environment.BASE_URL_PUBLIC}/categories`;
   private GET_USER_DTO = `${environment.BASE_URL}/user`;

   private getPostsByCategoryUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/category/${id}/posts`;
   }

   private getCommentsByPostUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/post/${id}`;
   }

   private getCategoryByIdUrl(id: number): string {
      return `${environment.BASE_URL_PUBLIC}/category/${id}`;
   }

   //category is required to publish a post
   //private POST_POST_URL = `${environment.BASE_URL}\\post`;


   //private AUTH_HEADER_CONTENT = "Bearer: " + this.localStorage.retrieve('token');

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   getCurrentUser() {
      return this.http.get<any>(this.GET_USER_DTO + "/current/dto", { headers: this.headersObj });
   }

   getAllPosts() {
      return this.http.get<Post[]>(this.GET_POSTS);
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
      return this.http.get<Post[]>(this.getPostsByCategoryUrl(categoryId));
   }

   getCommentsByPost(postId: number) {
      return this.http.get<Post>(this.getCommentsByPostUrl(postId));
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

   createNewPost(post: Post): Observable<any> {

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
