import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders } from '@angular/common/http';

import { PostModel } from '../model/post-model';
import { CategoryModel } from '../model/category-model';
import { CommentModel } from '../model/comment-model';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';
import { UserModel } from '../model/user-model';

@Injectable({
   providedIn: 'root'
})
export class UserApiService {

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   getCurrentUser() {
      return this.http.get<UserModel>(`${environment.BASE_URL}/user/current`, { headers: this.headersObj });
   }

   getUserByUsername(username: string) {
      return this.http.get<UserModel>(`${environment.BASE_URL}/user/${username}`);
   }

   getPostsByUsername(username: string) {
      return this.http.get<PostModel[]>(`${environment.BASE_URL_PUBLIC}/user/${username}/posts`);
   }

   getCommentsByUsername(username: string) {
      return this.http.get<CommentModel[]>(`${environment.BASE_URL_PUBLIC}/user/${username}/comments`);
   }

   getFollowedCategoriesByUsername(username: string) {
      return this.http.get<CategoryModel[]>(`${environment.BASE_URL_PUBLIC}/user/${username}/followed-categories`);
   }

   private headersObj = new HttpHeaders({
      'Authorization': "Bearer " + this.cookieService.get('token'),
      'Access-Control-Allow-Origin': 'http://localhost:8080',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type:application/json',
      'Access-Control-Allow-Credentials': 'true'
   });
}
