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
export class CategoryApiService {

   constructor(private http: HttpClient, private cookieService: CookieService) { }

   //todo change cat entity to include description
   getAllCategories() {
      return this.http.get<CategoryModel[]>(`${environment.BASE_URL_PUBLIC}/categories`);
   }

   getCategoryById(categoryId: number) {
      return this.http.get<CategoryModel>(`${environment.BASE_URL_PUBLIC}/category/${categoryId}`);
   }

   getCategoryByIdSecured(cateogryId: number) {
      return this.http.get<CategoryModel>(`${environment.BASE_URL}/category/${cateogryId}`, { headers: this.headersObj });
   }

   followCategory(cateogryId: number) {
      return this.http.get<any>(`${environment.BASE_URL}/category/${cateogryId}/follow`, { headers: this.headersObj });
   }

   unfollowCategory(cateogryId: number) {
      return this.http.get<any>(`${environment.BASE_URL}/category/${cateogryId}/unfollow`, { headers: this.headersObj });
   }

   private headersObj = new HttpHeaders({
      'Authorization': "Bearer " + this.cookieService.get('token'),
      'Access-Control-Allow-Origin': 'http://localhost:8080',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE',
      'Access-Control-Allow-Headers': 'X-Requested-With,content-type:application/json',
      'Access-Control-Allow-Credentials': 'true'
   });
}
