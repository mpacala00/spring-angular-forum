import { Injectable } from '@angular/core';

import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

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

   getUserProfileInfo(username: string) {
      return this.http.get<UserModel>(`${environment.BASE_URL_PUBLIC}/user/${username}`);
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

   blockUser(userId: number) {
      return this.http.get<any>(`${environment.BASE_URL}/user/${userId}/block`);
   }
}
